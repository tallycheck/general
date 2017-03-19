package com.taoswork.tallycheck.junitext.rules;


import org.junit.internal.runners.statements.FailOnTimeout;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

/**
 *
 */
public class MultiTestRule implements TestRule {
    @Retention(RetentionPolicy.RUNTIME)
    @Target({java.lang.annotation.ElementType.METHOD, java.lang.annotation.ElementType.TYPE})
    public @interface Repeat {
        //repeat times
        int value() default 1;

        //timeout for each step
        long timeout() default 0L;

        boolean parallel() default false;

        boolean printStep() default false;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target({java.lang.annotation.ElementType.METHOD})
    public @interface TimeoutOverride {
        //timeout for each step
        long value() default 0L;

        int times() default 1;
    }

    private boolean enableTimeout() {
        return !"true".equals(System.getProperty("maven.surefire.debug"));
    }

    @Override
    public Statement apply(Statement statement, Description description) {
        Statement result = statement;
        Class<?> testClass = description.getTestClass();
        String methodName = testClass.getSimpleName() + "#" + description.getMethodName();
        Repeat repeat = description.getAnnotation(Repeat.class);
        if (repeat == null) {
            repeat = (Repeat) testClass.getAnnotation(Repeat.class);
        }
        if (repeat != null) {
            int times = repeat.value();
            long timeout = repeat.timeout();
            TimeoutOverride timeoutOverride = description.getAnnotation(TimeoutOverride.class);
            if (timeoutOverride != null) {
                if (timeoutOverride.value() > 0) {
                    timeout = timeoutOverride.value();
                }
                timeout *= timeoutOverride.times();
            }
            if (timeout > 0 && enableTimeout()) {
                result = new FailOnTimeout(result, timeout);
            }
            if (times > 1) {
                result = new RepeatStatement(repeat, result, methodName);
            }
        }
        return result;
    }

    private static class RepeatStatement extends Statement {

        private final int times;
        private final boolean parallel;
        private final boolean printStep;
        private final Statement statement;
        private final String methodName;

        private RepeatStatement(Repeat repeat, Statement statement, String methodName) {
            this.times = repeat.value();
            this.parallel = repeat.parallel();
            this.printStep = repeat.printStep();
            this.statement = statement;
            this.methodName = methodName;
        }

        @Override
        public void evaluate() throws Throwable {
            long start = System.currentTimeMillis();
            if (parallel) {
                final CountDownLatch done = new CountDownLatch(times);
                final ExecutorService es = Executors.newCachedThreadPool();
                final AtomicReference<Throwable> anyError = new AtomicReference<>(null);
                try {
                    for (int i = 1; i <= times; i++) {
                        final int idx = i;
                        es.submit(
                                new Runnable() {
                                    @Override
                                    public void run() {
                                        try {
                                            if (anyError.compareAndSet(null, null)) {
                                                if (printStep) {
                                                    System.out.println("Repeat(Parallel) " + methodName + ": " + idx + "/" + times);
                                                }
                                                statement.evaluate();
                                            }
                                        } catch (Throwable throwable) {
                                            anyError.compareAndSet(null, throwable);
                                        } finally {
                                            done.countDown();
                                        }
                                    }
                                }
                        );
                    }
                    done.await();
                    if (!anyError.compareAndSet(null, null)) {
                        throw anyError.get();
                    }
                } finally {
                    es.shutdown();
                }
            } else {
                for (int i = 1; i <= times; i++) {
                    long lapsed = System.currentTimeMillis() - start;
                    long each = (i == 1 ? 0 : lapsed / (i - 1));
                    if (printStep) {
                        System.out.println("Repeat " + methodName + ": " + i + "/" + times + " elapsed(ms):" + lapsed + " each(ms):" + each);
                    }
                    statement.evaluate();
                }
            }
        }
    }
}

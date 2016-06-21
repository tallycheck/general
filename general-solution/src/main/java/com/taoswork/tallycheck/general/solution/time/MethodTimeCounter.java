package com.taoswork.tallycheck.general.solution.time;

import org.slf4j.Logger;

/**
 * Created by Gao Yuan on 2015/11/10.
 */
public class MethodTimeCounter extends TimeCounter {
    private final Logger logger;
    private final String action;

    public MethodTimeCounter(Logger logger) {
        this(logger, "");
    }

    public MethodTimeCounter(Logger logger, String action) {
        super();
        this.action = action;
        this.logger = logger;
    }

    public void noticeOnExit() {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stacktrace[2];
        String methodName = e.getMethodName();
        logger.info("{}:{} cost {} seconds.", methodName, action, getPassedInSeconds());
    }

    public void noticePerActionCostOnExit(int times) {
        StackTraceElement[] stacktrace = Thread.currentThread().getStackTrace();
        StackTraceElement e = stacktrace[2];
        String methodName = e.getMethodName();
        double passedInSecs = getPassedInSeconds();
        logger.info("{}:{} cost {} seconds. each: {}s", methodName, action, passedInSecs, passedInSecs / times);
    }
}
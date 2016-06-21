package com.taoswork.tallycheck.general.solution.threading.annotations;

/**
 * Created by Gao Yuan on 2015/8/12.
 */
public @interface GuardedBy {
    String value();

    int lockOrder() default 1;
}

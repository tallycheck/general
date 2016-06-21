package com.taoswork.tallycheck.general.solution.autotree;

/**
 * Created by Gao Yuan on 2015/5/25.
 */
public interface ICallableChecker<P> {
    boolean callable(P parameter);
}

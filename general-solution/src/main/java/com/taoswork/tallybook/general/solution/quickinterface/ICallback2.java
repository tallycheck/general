package com.taoswork.tallybook.general.solution.quickinterface;

/**
 * Created by Gao Yuan on 2015/5/24.
 */
public interface ICallback2<RT, PT, PT2,  ET extends Throwable> {
    RT callback(PT parameter, PT2 parameter2) throws ET;
}

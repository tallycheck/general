package com.taoswork.tallybook.general.solution.quickinterface;

/**
 * Created by Gao Yuan on 2015/5/24.
 */
public interface ICallback<RT, PT, ET extends Throwable> {
    RT callback(PT parameter) throws ET;
}

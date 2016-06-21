package com.taoswork.tallycheck.general.solution.time;

import java.util.Date;

/**
 * Created by Gao Yuan on 2015/11/10.
 */
public class TimeCounter {
    private long startTime;

    public TimeCounter(){
        reset();
    }

    public TimeCounter reset(){
        startTime = new Date().getTime();
        return this;
    }

    public long getPassed(){
        long now = new Date().getTime();
        return now - startTime;
    }

    public double getPassedInSeconds(){
        return 0.001 * getPassed();
    }
}

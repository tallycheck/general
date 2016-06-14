package com.taoswork.tallybook.general.solution.time;

/**
 * Created by Gao Yuan on 2015/6/18.
 */
public class IntervalSensitive {
    public static long NEVER_EXPIRE = -1L;
    public static long ALWAYS_EXPIRE = 0;

    private final long intervalThreshold;
    protected volatile long lastTouch = 0;

    public IntervalSensitive(long intervalThreshold) {
        this.intervalThreshold = intervalThreshold;
    }

    public long getIntervalThreshold() {
        return intervalThreshold;
    }

    /**
     * Package method, no public
     * @return the access ms, for (NEVER_EXPIRE & ALWAYS_EXPIRE) returns 0
     */
    long getLastTouch() {
        return lastTouch;
    }

    long touch(){
        lastTouch = System.currentTimeMillis();
        return lastTouch;
    }

    public boolean checkExpired() {
        return checkExpired(true);
    }

    public boolean checkExpired(boolean touch) {
        if (intervalThreshold < 0) {
            return false;
        } else  if (intervalThreshold == ALWAYS_EXPIRE) {
            onExpireOccur();
            return true;
        } else {
            long checkingTime = System.currentTimeMillis();
            try {
                if ((checkingTime - lastTouch) >= intervalThreshold) {
                    onExpireOccur();
                    return true;
                } else {
                    return false;
                }
            } finally {
                if(touch)
                    lastTouch = checkingTime;
            }
        }
    }

    protected void onExpireOccur(){

    }
}
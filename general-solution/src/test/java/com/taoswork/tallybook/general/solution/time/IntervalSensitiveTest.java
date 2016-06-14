package com.taoswork.tallybook.general.solution.time;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by Gao Yuan on 2015/6/18.
 */
public class IntervalSensitiveTest {
    @Test
    public void neverExpire() throws Exception{
        IntervalSensitive intervalSensitive = new IntervalSensitive(IntervalSensitive.NEVER_EXPIRE);
        Assert.assertEquals(intervalSensitive.getLastTouch(), 0L);
        Assert.assertFalse(intervalSensitive.checkExpired());
        Assert.assertEquals(intervalSensitive.getLastTouch(), 0L);
        Thread.sleep(10);
        Assert.assertFalse(intervalSensitive.checkExpired());
        Thread.sleep(100);
        Assert.assertFalse(intervalSensitive.checkExpired());
    }

    @Test
    public void alwaysExpire() throws Exception{
        IntervalSensitive intervalSensitive = new IntervalSensitive(IntervalSensitive.ALWAYS_EXPIRE);
        Assert.assertEquals(intervalSensitive.getLastTouch(), 0L);
        Assert.assertTrue(intervalSensitive.checkExpired());
        Assert.assertEquals(intervalSensitive.getLastTouch(), 0L);
        Thread.sleep(10);
        Assert.assertTrue(intervalSensitive.checkExpired());
        Thread.sleep(100);
        Assert.assertTrue(intervalSensitive.checkExpired());
    }

    @Test(timeout = 10000)
    public void sometimeExpire() throws Exception{
        IntervalSensitive intervalSensitive = new IntervalSensitive(1000);
        Assert.assertEquals(intervalSensitive.getLastTouch(), 0L);
        Assert.assertTrue(intervalSensitive.checkExpired());
        Assert.assertNotEquals(intervalSensitive.getLastTouch(), 0L);
//        long lastTouch = intervalSensitive.getLastTouch();

        final long lagTolerance = 10;
        int successed = 0;
        int tested = 0;

        for(int i = 0 ; i < 100 ; i++){
            boolean success = true;
            success &= shouldTrue(intervalSensitive, lagTolerance, 10, false);
            success &= shouldTrue(intervalSensitive, lagTolerance, 100, false);
            success &= shouldTrue(intervalSensitive, lagTolerance, 1000, true);
            success &= shouldTrue(intervalSensitive, lagTolerance, 1010, true);
            if(success)
                successed++;
            tested ++;
            if(1.0 * successed /tested > 0.95)
                return;
        }
        Assert.fail();
    }

    private boolean shouldTrue(IntervalSensitive intervalSensitive, long lagTolerance, long expectedInterval, boolean expireExpect) throws InterruptedException {
        long lastTouch = intervalSensitive.touch();
        boolean noError = true;
        Thread.sleep(expectedInterval);
        boolean expired = intervalSensitive.checkExpired();
        noError = (expireExpect == expired);
        //get now
        long currentAccess = intervalSensitive.getLastTouch();
        //check interval
        long interval = currentAccess - lastTouch;

        noError &= (interval >= expectedInterval);
        noError &= (interval < (expectedInterval + lagTolerance));
        return noError;
    }

}

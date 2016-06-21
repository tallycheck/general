package com.taoswork.tallycheck.general.extension.utils;

/**
 * Created by Gao Yuan on 2015/5/26.
 */
public class BitUtility {

    public static boolean fullyMatch(int input, int mask){
        return input == mask;
    }

    public static boolean containsAll(int input, int mask){
        return (input & mask) == mask;
    }

    public static boolean containsAny(int input, int mask){
        return (input & mask) != 0x0;
    }

    public static int excludeMask(int input, int mask){
        return input & (~mask);
    }
}

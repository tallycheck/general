package com.taoswork.tallybook.general.extension.utils;

/**
 * Created by Gao Yuan on 2015/5/13.
 */
public class DateUtility {
    public static long getValue(long valueTreatAsEmpty, long emptyReplacement, long currentValue){
        if(currentValue == valueTreatAsEmpty){
            return emptyReplacement;
        }
        return currentValue;
    }
}

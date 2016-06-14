package com.taoswork.tallybook.general.extension.utils;

/**
 * Created by Gao Yuan on 2015/6/2.
 */
public class CompareUtility {
    public static boolean isSame(Object a, Object b){
        if(a == null && b == null){
            return true;
        }else if(a == null){
            return false;
        }else if(b == null){
            return false;
        }else{
            return a.equals(b);
        }
    }
}

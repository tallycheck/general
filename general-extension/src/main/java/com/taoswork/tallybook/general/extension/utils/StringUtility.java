package com.taoswork.tallybook.general.extension.utils;

/**
 * Created by Gao Yuan on 2015/5/13.
 */
public class StringUtility {
    public static String changeFirstCharUpperLowerCase(String str){
        String firstChar = "" + str.charAt(0);
        String FirstChar = firstChar.toUpperCase();
        String changedFirstChar = FirstChar.equals(firstChar) ? firstChar.toLowerCase() : firstChar.toUpperCase();
        return changedFirstChar + str.substring(1);
    }

    public static String lastPiece(String source, String separator){
        int index = source.lastIndexOf(separator);
        if(index < 0){
            return source;
        }
        return source.substring(index + 1);
    }
}

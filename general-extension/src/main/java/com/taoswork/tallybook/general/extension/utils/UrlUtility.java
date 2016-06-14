package com.taoswork.tallybook.general.extension.utils;

import java.util.Map;

/**
 * Created by Gao Yuan on 2015/4/21.
 */
public class UrlUtility {
    public static <T> T findLongestPrefixMatchingValue(
            String requested, Map<String, T> prefixValueMapping,
            T escapeValue , T defaultValue){
        String longestPrefix = "";
        T matchingValue = null;

        for (Map.Entry<String, T> entry : prefixValueMapping.entrySet()) {
            String prefix = entry.getKey();

            if (prefix.length() > longestPrefix.length()) {
                if (requested.startsWith(prefix)) {
                    longestPrefix = prefix;

                    T value = entry.getValue();
                    if (!value.equals(escapeValue)) {
                        matchingValue = value;
                    }
                }
            }
        }

        if (longestPrefix.equals("")) {
            matchingValue = defaultValue;
        }

        return matchingValue;
    }
}

package com.taoswork.tallybook.general.extension.collections;

import java.util.Map;

/**
 * Created by Gao Yuan on 2015/4/28.
 */
public class MapUtility {
    public static <K, V> void putIfAbsent(final Map<K, V> from, final Map<K, V> to){
        for(Map.Entry<K,V> entry : from.entrySet()){
            to.putIfAbsent(entry.getKey(), entry.getValue());
        }
    }
}

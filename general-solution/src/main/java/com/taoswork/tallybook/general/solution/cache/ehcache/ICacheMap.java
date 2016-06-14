package com.taoswork.tallybook.general.solution.cache.ehcache;

import java.util.Map;

/**
 * Created by Gao Yuan on 2015/6/16.
 */
public interface ICacheMap<K, V>
        extends Map<K, V> {
    String getScopeName();
}

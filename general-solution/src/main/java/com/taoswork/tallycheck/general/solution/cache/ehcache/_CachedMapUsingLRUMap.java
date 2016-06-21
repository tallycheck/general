package com.taoswork.tallycheck.general.solution.cache.ehcache;


import org.apache.commons.collections4.map.LRUMap;

class _CachedMapUsingLRUMap extends LRUMap implements ICacheMap {
    protected final String scopeName;

    public _CachedMapUsingLRUMap(String scopeName) {
        this.scopeName = scopeName;
    }

    @Override
    public String getScopeName() {
        return scopeName;
    }


}

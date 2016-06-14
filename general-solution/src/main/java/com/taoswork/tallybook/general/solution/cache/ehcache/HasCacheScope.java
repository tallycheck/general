package com.taoswork.tallybook.general.solution.cache.ehcache;

/**
 * Created by Gao Yuan on 2015/6/16.
 */
public interface HasCacheScope {
    String getCacheScope();

    void setCacheScope(String scope);
}

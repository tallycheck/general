package com.taoswork.tallybook.general.solution.cache.ehcache;

import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;

import java.util.*;

/**
 * Created by Gao Yuan on 2015/6/16.
 */
class _CacheMapUsingEhcache<K, V>
        implements ICacheMap<K, V> {
    private final String scopeName;
    private final Cache cache;

    public _CacheMapUsingEhcache(String scopeName, Cache cache) {
        this.scopeName = scopeName;
        this.cache = cache;
    }

    @Override
    public String getScopeName() {
        return scopeName;
    }

    @Override
    public int size() {
        return cache.getSize();
    }

    @Override
    public boolean isEmpty() {
        return cache.getSize() == 0;
    }

    @Override
    public boolean containsKey(Object key) {
        Element element = cache.get(key);
        if (null == element) {
            return false;
        }
        return true;
    }

    @Override
    public boolean containsValue(Object value) {
        throw new UnsupportedOperationException("_CacheMapUsingEhcache.containsValue() not supported");
    }

    @Override
    public V get(Object key) {
        Element element = cache.get(key);
        if (null == element) {
            return null;
        }
        return (V) element.getObjectValue();
    }

    @Override
    public V put(K key, V value) {
        Element element = cache.get(key);
        if (null == element) {
            cache.put(new Element(key, value));
            return null;
        } else {
            cache.put(new Element(key, value));
            return (V) element.getObjectValue();
        }
    }

    @Override
    public V remove(Object key) {
        Element element = cache.get(key);
        if (null == element) {
            return null;
        } else {
            cache.remove(key);
            return (V) element.getObjectValue();
        }
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        int s = m.size();
        if (s > 0) {
            for (Map.Entry<? extends K, ? extends V> e : m.entrySet()) {
                K key = e.getKey();
                V value = e.getValue();
                cache.put(new Element(key, value));
            }
        }
    }

    @Override
    public void clear() {
        cache.removeAll();
    }

    @Override
    public Set<K> keySet() {
        List<K> keys = cache.getKeysNoDuplicateCheck();
        Set<K> keyst = new HashSet<K>();
        keyst.addAll(keys);
        return keyst;
    }

    @Override
    public Collection<V> values() {
        throw new UnsupportedOperationException("_CacheMapUsingEhcache.containsValue() not supported");
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException("_CacheMapUsingEhcache.containsValue() not supported");
    }


//
//    @Override
//    public void forEach(BiConsumer<? super K, ? super V> action) {
//        throw new UnsupportedOperationException("_CacheMapUsingEhcache.containsValue() not supported");
//    }
}

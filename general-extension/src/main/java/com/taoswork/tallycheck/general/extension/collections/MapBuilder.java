package com.taoswork.tallycheck.general.extension.collections;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by Gao Yuan on 2015/4/21.
 * Use new MapBuilder(HashMap<K, V> map) to create Builder
 * Use append() to build up Map
 */
public class MapBuilder<K, V> implements Map<K, V> {
    private final Map<K, V> innerMap;

    public MapBuilder(){
        this(new HashMap<K, V>());
    }

    public MapBuilder(HashMap<K, V> map){
        innerMap = map;
    }

    @Override
    public int size() {
        return innerMap.size();
    }

    @Override
    public boolean isEmpty() {
        return innerMap.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return innerMap.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value) {
        return innerMap.containsValue(value);
    }

    @Override
    public V get(Object key) {
        return innerMap.get(key);
    }

    @Override
    public V put(K key, V value) {
        return innerMap.put(key, value);
    }

    @Override
    public V remove(Object key) {
        return innerMap.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        innerMap.putAll(m);
    }

    @Override
    public void clear() {
        innerMap.clear();
    }

    @Override
    public Set<K> keySet() {
        return innerMap.keySet();
    }

    @Override
    public Collection<V> values() {
        return innerMap.values();
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        return innerMap.entrySet();
    }

    public MapBuilder<K, V> append(K key, V value){
        innerMap.put(key, value);
        return this;
    }

}

package com.taoswork.tallybook.general.solution.threading.lock;

import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Gao Yuan on 2015/8/16.
 */
public abstract class ReadWriteLockedMapAccess<K, V> {
    private final ReadWriteLock rwl = new ReentrantReadWriteLock();
    private final Lock r = rwl.readLock();
    private final Lock w = rwl.readLock();

    private final Map<K, V> map;

    public ReadWriteLockedMapAccess(Map<K, V> map) {
        this.map = map;
    }

    public V getCachedData(K key) {
        r.lock();
        V value = getCachedValueDirectly(key);
        if (!isCacheValid(key, value)) {
            r.unlock();
            w.lock();
            try {
                //recheck status because another thread might have acquired write lock
                value = getCachedValueDirectly(key);
                if (!isCacheValid(key, value)) {
                    value = generateValueForKey(key);
                    doCacheValue(key, value);
                }// Downgrade by acquiring read lock before releasing write lock
                r.lock();
            } finally {
                w.unlock(); // Unlock write, still hold read
            }
        }

        try {
            return value;
        } finally {
            r.unlock();
        }
    }

    protected final V getCachedValueDirectly(K key) {
        return map.get(key);
    }

    protected final boolean isCacheValid(K key, V value) {
        return value != null;
    }

    protected final void doCacheValue(K key, V value) {
        map.put(key, value);
    }

    protected abstract V generateValueForKey(K key);

}

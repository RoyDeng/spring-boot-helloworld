package com.roydevelop.helloworld.cache;

import java.util.Map;

import com.roydevelop.helloworld.cache.constant.CacheConstant;
import com.roydevelop.helloworld.cache.map.ExpiryMap;

public class ExpiryCache implements Cache {
    private Map<Object, Object> cache = new ExpiryMap<>();

    public ExpiryCache() {
        cache = new ExpiryMap<>(CacheConstant.DEFAULT_TIME);
    }

    public ExpiryCache(long expiryTime) {
        this.cache = new ExpiryMap<>(expiryTime);
    }

    @Override
    public void putObject(Object key, Object value) {
        cache.put(key, value);
    }

    @Override
    public Object getObject(Object key) {
        return cache.get(key);
    }

    @Override
    public Object removeObject(Object key) {
        return cache.remove(key);
    }

    @Override
    public String toString() {
        return cache.toString();
    }
}

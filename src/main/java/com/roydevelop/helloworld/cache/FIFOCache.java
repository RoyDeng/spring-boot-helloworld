package com.roydevelop.helloworld.cache;

import java.util.Deque;
import java.util.LinkedList;

public class FIFOCache implements Cache {
    private Cache cache;

    private Deque<Object> keyOrderList;

    private int maxCap;

    public FIFOCache(int maxCap, Cache cache) {
        this.maxCap = maxCap;
        this.cache = cache;
        keyOrderList = new LinkedList<Object>();
    }

    @Override
    public void putObject(Object key, Object value) {
        keyOrderList.addLast(key);

        if (keyOrderList.size() > maxCap) {
            Object oldkey = keyOrderList.removeFirst();
            cache.removeObject(oldkey);
        }

        cache.putObject(key, value);
    }

    @Override
    public Object getObject(Object key) {
        return cache.getObject(key);
    }

    @Override
    public Object removeObject(Object key) {
        return cache.removeObject(key);
    }

    @Override
    public String toString() {
        return cache.toString();
    }
}

package com.roydevelop.helloworld.cache;

import java.util.Deque;
import java.util.LinkedList;

public class LRUCache implements Cache {
    private Cache cache;

    private Deque<Object> keyOrderList;

    private int maxCap;

    public LRUCache(int maxCap, Cache cache) {
        this.maxCap = maxCap;
        this.cache = cache;
        keyOrderList = new LinkedList<Object>();
    }

    @Override
    public void putObject(Object key, Object value) {
        keyOrderList.addLast(key);

        if (keyOrderList.size() > maxCap) {
            Object oldKey = keyOrderList.removeFirst();
            cache.removeObject(oldKey);
        }

        cache.putObject(key, value);
    }

    @Override
    public Object getObject(Object key) {
        Object obj = cache.getObject(key);

        if (obj != null) {
            keyOrderList.remove(key);
            keyOrderList.addLast(key);
        }

        return obj;
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

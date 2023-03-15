package com.roydevelop.helloworld.cache;

public class LogCache implements Cache {
    private Cache cache;

    private int requests;

    private int hit;

    public LogCache(Cache cache) {
        this.cache = cache;
    }

    @Override
    public void putObject(Object key, Object value) {
        cache.putObject(key, value);
    }

    @Override
    public Object getObject(Object key) {
        requests++;
        
        Object obj = cache.getObject(key);

        if (obj != null) hit++;

        System.out.println("Cache request hits" + hit * 1.0 / requests);

        return obj;
    }

    @Override
    public Object removeObject(Object key) {
        return cache.removeObject(key);
    }
}

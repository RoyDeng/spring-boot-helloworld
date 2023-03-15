package com.roydevelop.helloworld.cache;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LFUCache implements Cache {
    private Cache cache;

    private List<Object> keyOrderList;

    private Map<Object, HitRate> count;

    private int maxCap;

    public LFUCache(int maxCap, Cache cache) {
        this.maxCap = maxCap;
        this.cache = cache;
        count = new HashMap<>();
        keyOrderList = new ArrayList<>();
    }

    private Object removeElement() {
        HitRate hr = Collections.min(count.values());
        keyOrderList.remove(hr.key);
        count.remove(hr.key);
        return cache.removeObject(hr.key);
    }

    private void addHitCount(Object key) {
        HitRate hitRate = count.get(key);
        hitRate.hitCount = hitRate.hitCount + 1;
        hitRate.lastTime = System.nanoTime();
    }

    @Override
    public void putObject(Object key, Object value) {
        Object v = cache.getObject(key);

        if (v == null) {
            if (keyOrderList.size() == maxCap) {
                removeElement();
            }
            count.put(key, new HitRate(key, 1, System.nanoTime()));
        } else {
            addHitCount(key);
        }

        keyOrderList.add(key);
        cache.putObject(key, value);
    }

    @Override
    public Object getObject(Object key) {
        Object value = cache.getObject(key);

        if (value != null) {
            addHitCount(key);
            return value;
        }

        return removeElement();
    }

    @Override
    public Object removeObject(Object key) {
        Object value = cache.getObject(key);

        if (value == null) {
            return null;
        }

        return removeElement();
    }

    class HitRate implements Comparable<HitRate> {
        private Object key;
        private int hitCount;
        private long lastTime;

        private HitRate(Object key, int hitCount, long lastTime) {
            this.key = key;
            this.hitCount = hitCount;
            this.lastTime = lastTime;
        }

        @Override
        public int compareTo(HitRate o) {
            int compare = Integer.compare(this.hitCount, o.hitCount);
            return compare == 0 ? Long.compare(this.lastTime, o.lastTime) : compare;
        }
    }
}

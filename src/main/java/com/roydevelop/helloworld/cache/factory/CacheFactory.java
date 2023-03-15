package com.roydevelop.helloworld.cache.factory;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Component;

import com.roydevelop.helloworld.cache.Cache;
import com.roydevelop.helloworld.cache.ExpiryCache;
import com.roydevelop.helloworld.cache.FIFOCache;
import com.roydevelop.helloworld.cache.LogCache;
import com.roydevelop.helloworld.cache.PerpetualCache;
import com.roydevelop.helloworld.cache.SynchronizedCache;
import com.roydevelop.helloworld.cache.constant.CacheConstant;
import com.roydevelop.helloworld.cache.exception.CacheException;
import com.roydevelop.helloworld.cache.vo.CacheVo;

@Component
public class CacheFactory {
    private final ConcurrentHashMap<String, Cache> cachePools = new ConcurrentHashMap<>();

    public Cache produce(CacheVo cacheVo) {
        if (cachePools.containsKey(cacheVo.getCacheName())) {
            return cachePools.get(cacheVo.getCacheName());
        }

        Cache cache = null;

        cache = cacheVo.getExpire() ? new ExpiryCache(cacheVo.getExpireTime()) : new PerpetualCache();

        switch (cacheVo.getType().toLowerCase()) {
            case CacheConstant.FIFO:
                cache = new FIFOCache(cacheVo.getCapacity(), cache);
                break;
            default:
                throw new CacheException("");
        }

        if (cacheVo.getLog()) cache = new LogCache(cache);

        if (cacheVo.getSync()) cache = new SynchronizedCache(cache);

        cachePools.put(cacheVo.getCacheName(), cache);

        return cache;
    }
}

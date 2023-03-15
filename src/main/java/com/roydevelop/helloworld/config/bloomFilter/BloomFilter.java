package com.roydevelop.helloworld.config.bloomFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;

@Service
public class BloomFilter {
    @Autowired
    private RedisTemplate redisTemplate;

    public <T> void addByBloomFilter(BloomFilterHelper<T> bloomFilterHelper, String key,T value){
        Preconditions.checkArgument(bloomFilterHelper != null, "bloomFilterHelper不能为空");
        int[] offset = bloomFilterHelper.murmurHashOffset(value);
        for (int i : offset) {
//            System.out.println("key : " + key + " " + "value : " + i);
            redisTemplate.opsForValue().setBit(key,i,true);
        }
    }

    public <T> boolean includeByBloomFilter(BloomFilterHelper<T> bloomFilterHelper, String key,T value){
        Preconditions.checkArgument(bloomFilterHelper != null, "bloomFilterHelper不能为空");
        int[] offset = bloomFilterHelper.murmurHashOffset(value);
        for (int i : offset) {
//            System.out.println("key : " + key + " " + "value : " + i);
            if (!redisTemplate.opsForValue().getBit(key,i)){
                return false;
            }
        }
        return true;
    }
}

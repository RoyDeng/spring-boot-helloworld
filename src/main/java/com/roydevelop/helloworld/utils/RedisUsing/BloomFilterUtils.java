package com.roydevelop.helloworld.utils.RedisUsing;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.roydevelop.helloworld.config.BloomFilter.BloomFilterHelper;
import com.roydevelop.helloworld.config.BloomFilter.RedisBloomFilter;

@Component
public class BloomFilterUtils {
    @Autowired
    private RedisBloomFilter redisBloomFilter;

    @Autowired
    private BloomFilterHelper bloomFilterHelper;

    public <T> boolean addBloomFilter(String key, T value) {
        try {
            redisBloomFilter.addByBloomFilter(bloomFilterHelper, "bloom:" + key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public <T> boolean checkBloomFilter(String key, T value) {
        boolean exist= redisBloomFilter.includeByBloomFilter(bloomFilterHelper, "bloom:" + key, value);
        return exist;
    }
}

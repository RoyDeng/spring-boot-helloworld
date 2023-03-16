package com.roydevelop.helloworld.utils.RedisUsing;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import com.roydevelop.helloworld.dao.UserMapper;

@Component
public class RedisTemplateUtil {
    private final RedisTemplate redisTemplate;

    @Autowired
    private UserMapper userMapper;

    public RedisTemplateUtil(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public Object getHotkey(String key) {
        Object value = redisTemplate.opsForValue().get(key);
        if (value == null) {
            Object key_mutex = "lock";

            if (redisTemplate.opsForValue().setIfAbsent(key_mutex, "1",180,TimeUnit.SECONDS)) {
                System.out.println("Enable distributed lock!");
                value = userMapper.selectByPrimaryKey(Integer.valueOf(key));
                redisTemplate.opsForValue().set(key, value,3600, TimeUnit.MILLISECONDS);
                redisTemplate.delete(key_mutex);
            } else {
                try {
                    Thread.sleep(50);
                    System.out.println("Other threads are blocked!");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                getHotkey(key);
            }
        }
        return value;
    }
}

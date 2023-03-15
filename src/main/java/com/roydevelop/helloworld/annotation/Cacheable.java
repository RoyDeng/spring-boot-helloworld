package com.roydevelop.helloworld.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.core.annotation.AliasFor;

import com.roydevelop.helloworld.cache.constant.CacheConstant;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface Cacheable {
    @AliasFor("cacheName")
    String value() default "";

    @AliasFor("value")
    String cacheName() default "";

    String key() default "";

    String type() default CacheConstant.FIFO;

    int capacity() default CacheConstant.MAX_CAP;

    boolean log() default false;

    boolean sync() default false;

    boolean expire() default false;

    long expireTime() default CacheConstant.DEFAULT_TIME;
}

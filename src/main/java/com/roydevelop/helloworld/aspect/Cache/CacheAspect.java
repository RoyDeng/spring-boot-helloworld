package com.roydevelop.helloworld.aspect.Cache;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.roydevelop.helloworld.annotation.Cacheable;
import com.roydevelop.helloworld.cache.Cache;
import com.roydevelop.helloworld.cache.factory.CacheFactory;
import com.roydevelop.helloworld.cache.vo.CacheVo;

import lombok.extern.slf4j.Slf4j;

@Aspect
@Component
@Slf4j
public class CacheAspect {
    @Autowired
    private CacheFactory cacheFactory;

    @Pointcut("@annotation(com.roydevelop.helloworld.annotation.Cacheable)")
    public void doPointcut() {

    }

    @Around("doPointcut()")
    public Object arround(ProceedingJoinPoint jp) throws Throwable {
        try {
            Object cache = getCache(jp);

            if (cache != null) {
                return cache;
            } else {
                Object result = jp.proceed();
                setCache(jp, result);
                return result;
            }
        } catch (Throwable e) {
            log.error(e.getMessage());
            throw e;
        }
    }

    private Object getCache(ProceedingJoinPoint jp) {
        CacheVo cacheVo = getCacheItem(jp);
        Cache cache = cacheFactory.produce(cacheVo);
        return cache.getObject(cacheVo);
    }

    private void setCache(ProceedingJoinPoint jp, Object res) throws NoSuchFieldException {
        CacheVo cacheVo = getCacheItem(jp);
        Cache cache = cacheFactory.produce(cacheVo);
        cache.putObject(cacheVo.getKey(), res);
    }

    private CacheVo getCacheItem(ProceedingJoinPoint jp) {
        Class<?> targetCls = jp.getTarget().getClass();
        String group = targetCls.getName();
        MethodSignature ms = (MethodSignature) jp.getSignature();
        Method method = ms.getMethod();

        Cacheable annotation = method.getAnnotation(Cacheable.class);
        String cacheName = annotation.cacheName();
        String key = annotation.key();

        if (StringUtils.isEmpty(cacheName)) {
            cacheName = group;
        }

        String fieldName = "";
        boolean isField = key.contains("#");

        if (isField) {
            fieldName = key.replace("#", "fieldName");
        }

        Object[] args = jp.getArgs();

        if (args.length == 0 || StringUtils.isEmpty(key)) {
            key = method.getName();
        }

        if (isField) {
            String[] parameterNames = ms.getParameterNames();
            int index = Arrays.binarySearch(parameterNames, fieldName);
            Object object = args[index];
            key = String.valueOf(object);
        }

        return CacheVo.builder()
            .cacheName(cacheName)
            .key(key)
            .capacity(annotation.capacity())
            .type(annotation.type())
            .log(annotation.log())
            .sync(annotation.sync())
            .expire(annotation.expire())
            .ExpireTime(annotation.expireTime())
            .build();
    }
}

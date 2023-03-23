package com.roydevelop.helloworld.utils.common;

import java.text.SimpleDateFormat;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonUtil {
    private static ObjectMapper objectMapper = new ObjectMapper();
    private static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    static {
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.setDateFormat(new SimpleDateFormat(DATE_FORMAT));
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <T> String objToStr(T obj) {
        if (null == obj) {
            return null;
        }

        try {
            return obj instanceof String ? (String) obj : objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.warn("objToStr error: ", e);
            return null;
        }
    }

    public static <T> T strToObj(String str, Class<T> clazz) {
        if (StringUtils.isBlank(str) || null == clazz) {
            return null;
        }

        try {
            return clazz.equals(String.class) ? (T) str : objectMapper.readValue(str, clazz);
        } catch (Exception e) {
            log.warn("strToObj error: ", e);
            return null;
        }
    }

    public static <T> T strToObj(String str, TypeReference<T> typeReference) {
        if (StringUtils.isBlank(str) || null == typeReference) {
            return null;
        }

        try {
            return (T) (typeReference.getType().equals(String.class) ? str : objectMapper.readValue(str, typeReference));
        } catch (Exception e) {
            log.error("strToObj error", e);
            return null;
        }
    }
}

package com.roydevelop.helloworld.cache;

public interface Cache {
    void putObject(Object key, Object value);

    Object getObject(Object key);

    Object removeObject(Object key);
}

package com.roydevelop.helloworld.cache.exception;

public class CacheException extends RuntimeException {
    private static final long serialVersionUID = -234204541003512526L;

    public CacheException() {
        super();
    }

    public CacheException(String message) {
        super(message);
    }

    public CacheException(Throwable cause) {
        super(cause);
    }
}

package com.roydevelop.helloworld.cache.constant;

public class CacheConstant {
    public static final int MAX_CAP = 32;

    public static final long ONE_MINUTES = 1000 * 60L;

    private static final long TEN_MINUTES = ONE_MINUTES * 10L;

    private static final long HALF_AN_HOUR = ONE_MINUTES * 30L;

    private static final long ONE_HOUR = ONE_MINUTES * 60L;

    private static final long SIX_HOUR = ONE_HOUR * 6L;

    private static final long ONE_DAY = ONE_HOUR * 12L;

    public static final long DEFAULT_TIME = ONE_MINUTES + 5L;

    public static final String FIFO = "fifo";

    public static final String LRU = "lru";

    public static final String LFU = "lfu";
}

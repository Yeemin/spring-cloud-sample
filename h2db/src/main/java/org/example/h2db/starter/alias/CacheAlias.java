package org.example.h2db.starter.alias;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.core.util.ArrayUtil;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CacheAlias {

    private static final Map<String, Cache<String, String>> cacheMap = new ConcurrentHashMap<>();

    public static String showCacheNames() {
        return String.join(",", cacheMap.keySet());
    }

    public static String hasCacheName(String cacheName) {
        return cacheMap.containsKey(cacheName) ? "TRUE" : "FALSE";
    }

    public static String removeCacheName(String cacheName) {
        cacheMap.remove(cacheName);
        return "TRUE";
    }

    public static String fifoCache(String... inputs) {
        if (ArrayUtil.isEmpty(inputs)) {
            return "params is null";
        }
        CacheParams cacheParams = generateCacheParams(inputs);
        Cache<String, String> cache = CacheUtil.newFIFOCache(cacheParams.capacity, cacheParams.timeout);
        cacheMap.put(inputs[0], cache);
        return "create success";
    }

    public static String lruCache(String... inputs) {
        if (ArrayUtil.isEmpty(inputs)) {
            return "params is null";
        }
        CacheParams cacheParams = generateCacheParams(inputs);
        Cache<String, String> cache = CacheUtil.newLRUCache(cacheParams.capacity, cacheParams.timeout);
        cacheMap.put(inputs[0], cache);
        return "SUCCESS";
    }

    public static String lfuCache(String... inputs) {
        if (ArrayUtil.isEmpty(inputs)) {
            return "params is null";
        }
        CacheParams cacheParams = generateCacheParams(inputs);
        Cache<String, String> cache = CacheUtil.newLFUCache(cacheParams.capacity, cacheParams.timeout);
        cacheMap.put(inputs[0], cache);
        return "SUCCESS";
    }

    public static String timedCache(String... inputs) {
        if (ArrayUtil.isEmpty(inputs) || inputs.length < 2) {
            return "params is null";
        }
        long timeout = Long.parseLong(inputs[1]);
        Cache<String, String> cache = CacheUtil.newTimedCache(timeout);
        cacheMap.put(inputs[0], cache);
        return "SUCCESS";
    }

    public static String weakCache(String... inputs) {
        if (ArrayUtil.isEmpty(inputs) || inputs.length < 2) {
            return "params is null";
        }
        long timeout = Long.parseLong(inputs[1]);
        Cache<String, String> cache = CacheUtil.newWeakCache(timeout);
        cacheMap.put(inputs[0], cache);
        return "SUCCESS";
    }

    public static String cache(String cacheName, String key, String value) {
        if (cacheMap.containsKey(cacheName)) {
            cacheMap.get(cacheName).put(key, value);
        }
        return value;
    }

    public static String getCache(String cacheName, String key) {
        if (cacheMap.containsKey(cacheName)) {
            return cacheMap.get(cacheName).get(key);
        }
        return "no cache named " + cacheName;
    }

    public static String clearCache(String cacheName) {
        if (cacheMap.containsKey(cacheName)) {
            cacheMap.get(cacheName).clear();
        }
        return "TRUE";
    }

    public static String removeCache(String cacheName, String key) {
        if (cacheMap.containsKey(cacheName)) {
            cacheMap.get(cacheName).remove(key);
        }
        return "TRUE";
    }

    private static CacheParams generateCacheParams(String... inputs) {
        int length = inputs.length;
        int capacity = 1000;
        if (length > 1) {
            capacity = Integer.parseInt(inputs[1]);
        }
        long timeout = 0L;
        if (length > 2) {
            timeout = Long.parseLong(inputs[2]);
        }
        return new CacheParams(capacity, timeout);
    }

    static class CacheParams {
        int capacity;
        long timeout;

        public CacheParams(int capacity, long timeout) {
            this.capacity = capacity;
            this.timeout = timeout;
        }
    }

}

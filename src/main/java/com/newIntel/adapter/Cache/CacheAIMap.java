package com.newIntel.adapter.Cache;

import com.newIntel.adapter.bean.AiStatus;

import java.util.concurrent.ConcurrentHashMap;

public class CacheAIMap extends ConcurrentHashMap<String, AiStatus> {
    private volatile static CacheAIMap cacheAIMap;
    private CacheAIMap(){}
    public static CacheAIMap getCacheMap() {
        if (cacheAIMap == null) {
            synchronized (CacheAIMap.class) {
                if (cacheAIMap == null) {
                    cacheAIMap = new CacheAIMap();
                }
            }
        }
        return cacheAIMap;
    }
}

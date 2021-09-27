package com.newIntel.adapter.Cache;

import com.newIntel.adapter.bean.MaxViewDIStatus;

import java.util.concurrent.ConcurrentHashMap;

public class CacheDIMap extends ConcurrentHashMap<String, MaxViewDIStatus> {
    private volatile static CacheDIMap cacheDIMap;
    private CacheDIMap(){}
    public static CacheDIMap getCacheMap() {
        if (cacheDIMap == null) {
            synchronized (CacheDIMap.class) {
                if (cacheDIMap == null) {
                    cacheDIMap = new CacheDIMap();
                }
            }
        }
        return cacheDIMap;
    }
}
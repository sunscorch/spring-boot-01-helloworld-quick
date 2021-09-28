package com.newIntel.adapter.Cache;

import com.newIntel.adapter.bean.AiStatus;
import com.newIntel.adapter.bean.MaxViewSIData;

import java.util.concurrent.ConcurrentHashMap;

public class CacheSIMap extends ConcurrentHashMap<String, MaxViewSIData> {
    private volatile static CacheSIMap cacheSIMap;
    private CacheSIMap(){}
    public static CacheSIMap getCacheMap() {
        if (cacheSIMap == null) {
            synchronized (CacheSIMap.class) {
                if (cacheSIMap == null) {
                    cacheSIMap = new CacheSIMap();
                }
            }
        }
        return cacheSIMap;
    }
}

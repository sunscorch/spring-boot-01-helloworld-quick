package com.newIntel.adapter.Cache;

import com.newIntel.adapter.bean.MaxViewAlarm;
import com.newIntel.adapter.bean.MaxViewSIData;

import java.util.concurrent.ConcurrentHashMap;

public class CacheAlarmMap extends ConcurrentHashMap<String, MaxViewAlarm> {
    private volatile static CacheAlarmMap cacheAlarmMap;
    private CacheAlarmMap(){}
    public static CacheAlarmMap getCacheMap() {
        if (cacheAlarmMap == null) {
            synchronized (CacheAlarmMap.class) {
                if (cacheAlarmMap == null) {
                    cacheAlarmMap = new CacheAlarmMap();
                }
            }
        }
        return cacheAlarmMap;
    }
}

package com.newIntel.adapter.Cache;

import com.newIntel.adapter.bean.MaxViewCallRecord;

import java.util.concurrent.LinkedBlockingQueue;

public class CacheStructQueue extends LinkedBlockingQueue<MaxViewCallRecord>{
    private volatile static CacheStructQueue queue;
    private CacheStructQueue(){}
    public static CacheStructQueue getBlockingQueue(){
        if (queue == null) {
            synchronized (CacheStructQueue.class) {
                if (queue == null) {
                    queue = new CacheStructQueue();
                }
            }
        }
        return queue;
    }
}

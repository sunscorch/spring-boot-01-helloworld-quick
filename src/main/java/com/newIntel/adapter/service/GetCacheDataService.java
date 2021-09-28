package com.newIntel.adapter.service;

import com.newIntel.adapter.bean.*;

import java.util.List;

public interface GetCacheDataService {
    public List<AiStatus> getAiData();
    public List<DeviceStatus> getDeviceStatus();
    public List<ItcStatus> getItcStatus();
    public List<CallRecord> getCallRecords() throws InterruptedException;
    public List<FireDeviceStatus> getAlarms();
}

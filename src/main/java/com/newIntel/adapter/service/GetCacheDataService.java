package com.newIntel.adapter.service;

import com.newIntel.adapter.bean.AiStatus;
import com.newIntel.adapter.bean.DeviceStatus;
import com.newIntel.adapter.bean.MaxViewDIStatus;

import java.util.List;

public interface GetCacheDataService {
    public List<AiStatus> getAiData();
    public List<DeviceStatus> getDeviceStatus();
}

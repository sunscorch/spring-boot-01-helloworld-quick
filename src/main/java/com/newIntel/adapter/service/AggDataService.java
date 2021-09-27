package com.newIntel.adapter.service;

import com.newIntel.adapter.bean.MaxViewAIData;

import java.util.List;
import java.util.Map;

public interface AggDataService {
    public void aggAIdata(List<MaxViewAIData> mpList);

    public void aggDIdata(List<MaxViewAIData> mpList);
}

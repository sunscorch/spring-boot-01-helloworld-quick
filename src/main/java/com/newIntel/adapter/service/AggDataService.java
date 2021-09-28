package com.newIntel.adapter.service;

import com.newIntel.adapter.bean.MaxViewAIData;
import com.newIntel.adapter.bean.MaxViewCallRecord;
import com.newIntel.adapter.bean.MaxViewDIStatus;
import com.newIntel.adapter.bean.MaxViewSIData;

import java.util.List;
import java.util.Map;

public interface AggDataService {
    public void aggAIdata(List<MaxViewAIData> mpList);

    public void aggDIdata(List<MaxViewAIData> mpList);

    public void aggSIdata(List<MaxViewSIData> mpList);

    public void aggStructData(List<MaxViewCallRecord> mplist);
}

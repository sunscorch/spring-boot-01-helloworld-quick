package com.newIntel.adapter.service;

import com.newIntel.adapter.bean.AiStatus;
import jdk.nashorn.internal.runtime.ECMAException;

import java.util.List;

public interface PushData2TopService {
     void sendAiData() throws Exception;
     void sendDeviceData() throws Exception;
     void sendITCStatus() throws Exception;
     void sendCallRecords() throws  Exception;
     void sendFireDeviceStatus() throws Exception;
}

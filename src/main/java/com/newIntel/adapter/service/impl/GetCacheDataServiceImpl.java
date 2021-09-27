package com.newIntel.adapter.service.impl;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.newIntel.adapter.Cache.CacheAIMap;
import com.newIntel.adapter.Cache.CacheDIMap;
import com.newIntel.adapter.bean.AiStatus;
import com.newIntel.adapter.bean.DeviceStatus;
import com.newIntel.adapter.bean.MaxViewAIData;
import com.newIntel.adapter.bean.MaxViewDIStatus;
import com.newIntel.adapter.constant.Constant;
import com.newIntel.adapter.service.GetCacheDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;
import java.time.Instant;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class GetCacheDataServiceImpl implements GetCacheDataService {
    private static final Logger log = LoggerFactory.getLogger(GetCacheDataServiceImpl.class);
    @Override
    public List<AiStatus> getAiData() {
        List<AiStatus> resList = new ArrayList<AiStatus>();
        CacheAIMap cacheAIMap = CacheAIMap.getCacheMap();
        cacheAIMap.forEach((k, aiStatus) -> {
            //组装水泵数据
            DeviceStatus deviceStatus = findDeviceStatus(aiStatus);
            if(deviceStatus != null){
                aiStatus.setWaterPumpStatus(deviceStatus.getWaterPumpStatus());
            }
            aiStatus = checkSatleData(aiStatus);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            aiStatus.setDataTime(dtf.format(now));

            resList.add(aiStatus);

        });
        return resList;
    }

    //获取水泵信息
    @Override
    public List<DeviceStatus> getDeviceStatus() {
        List<DeviceStatus> resList = new ArrayList<DeviceStatus>();
        CacheDIMap dimap = CacheDIMap.getCacheMap();
        dimap.forEach((k, maxViewDIStatus) -> {
            String deviceId = k.substring(4,6);
            if(!deviceId.equals(Constant.WATERPUMP_OUTPUT_TYPE_CODE)){
                return;
            }

            DeviceStatus deviceStatus = checkandUpateDeviceStatus(maxViewDIStatus);
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu/MM/dd HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            deviceStatus.setDataTime(dtf.format(now));

            deviceStatus.setDeviceMac(k);
            resList.add(deviceStatus);

        });
        return resList;
    }

    private AiStatus checkSatleData(AiStatus aiStatus){
        long curTime = Instant.now().getEpochSecond();

        Long   carbonMonoxideArriveTime = aiStatus.getCarbonMonoxideArriveTime();
        Long  humidityArriveTime = aiStatus.getHumidityArriveTime();
        Long  hydrogenSulfideArriveTime = aiStatus.getHydrogenSulfideArriveTime();
        Long  methaneArriveTime = aiStatus.getMethaneArriveTime();
        Long  oxygenArriveTime = aiStatus.getOxygenArriveTime();
        Long  temperatureArriveTime = aiStatus.getTemperatureArriveTime();
        Long  waterPumpStatusArriveTime = aiStatus.getWaterPumpStatusArriveTime();

        long staleThreshold = 5L;
        if(carbonMonoxideArriveTime != null && (curTime-carbonMonoxideArriveTime/1000L)/60 >staleThreshold){
            aiStatus.setCarbonMonoxide(null);
        }

        if( humidityArriveTime != null && (curTime-humidityArriveTime/1000L)/60L >staleThreshold){
            aiStatus.setHumidity(null);
        }

        if(hydrogenSulfideArriveTime != null && (curTime-hydrogenSulfideArriveTime/1000L)/60L >staleThreshold){
            aiStatus.setHydrogenSulfide(null);
        }

        if(methaneArriveTime != null && (curTime-methaneArriveTime/1000L)/60L >staleThreshold){
            aiStatus.setMethane(null);
        }

        if(oxygenArriveTime != null && (curTime-oxygenArriveTime/1000L)/60L >staleThreshold){
            aiStatus.setOxygen(null);
        }

        if(temperatureArriveTime != null && (curTime-temperatureArriveTime/1000L)/60L >staleThreshold){
            aiStatus.setTemperature(null);
        }

        if(waterPumpStatusArriveTime != null && (curTime-waterPumpStatusArriveTime/1000L)/60L >staleThreshold){
            aiStatus.setWaterPumpStatus(Constant.DEVICE_OFFLINE);
        }

        return aiStatus;
    }

    private DeviceStatus checkandUpateDeviceStatus(MaxViewDIStatus maxViewDIStatus){

        long staleThreshold = 5L;
        long curTime = Instant.now().getEpochSecond();
        DeviceStatus deviceStatus = new DeviceStatus();
        Long  switchStatusArriveTime = maxViewDIStatus.getSwitchStatusArriveTime();


        if(switchStatusArriveTime != null && (curTime-switchStatusArriveTime/1000L)/60L >staleThreshold){
            deviceStatus.setWaterPumpStatus(Constant.DEVICE_OFFLINE);
        }

       deviceStatus.setWaterPumpStatus(maxViewDIStatus.getSwitchStatus());
        return deviceStatus;
    }

    private DeviceStatus findDeviceStatus(AiStatus aiStatus){
        CacheDIMap dimap = CacheDIMap.getCacheMap();
        DeviceStatus deviceStatus = null;
        for (Map.Entry<String, MaxViewDIStatus> entry : dimap.entrySet()) {
            String diKey = entry.getKey();
            String deviceTypeCode = diKey.substring(4,6);
            MaxViewDIStatus maxViewDIStatus = entry.getValue();

            if(!deviceTypeCode.equals(Constant.WATERPUMP_OUTPUT_TYPE_CODE)){
                continue;
            }

            String aiSurfix = aiStatus.getDeviceMac().substring(6,8);
            if(aiSurfix.equals(diKey.substring(8,10))){
                deviceStatus = checkandUpateDeviceStatus(maxViewDIStatus);
            }

        }
        return deviceStatus;
    }


}

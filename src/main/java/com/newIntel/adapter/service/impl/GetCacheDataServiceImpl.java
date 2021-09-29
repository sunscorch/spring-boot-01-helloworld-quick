package com.newIntel.adapter.service.impl;

import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import com.newIntel.adapter.Cache.*;
import com.newIntel.adapter.bean.*;
import com.newIntel.adapter.constant.Constant;
import com.newIntel.adapter.service.GetCacheDataService;
import com.newIntel.adapter.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.stereotype.Service;
import java.time.Instant;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
public class GetCacheDataServiceImpl implements GetCacheDataService {
    private static Object alarmLock = new Object();

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



    @Override
    public List<ItcStatus> getItcStatus() {
        CacheSIMap siMp= CacheSIMap.getCacheMap();
        List<ItcStatus> res = new ArrayList<>();
        for (Map.Entry<String, MaxViewSIData> entry : siMp.entrySet()) {
            String objId = entry.getKey();
            MaxViewSIData maxViewSIData = entry.getValue();
            ItcStatus itcStatus = new ItcStatus();

            //电话号码位最后两位
            itcStatus.setPhoneSip(objId.substring(8,10));
            //itcStatus.setWarningMessage("");
            LocalDateTime time = LocalDateTime.now();
            Date t = Date.from(time.atZone(Constant.DEFAULT_ZONE).toInstant());
            itcStatus.setWarningTime(t);

            if((System.currentTimeMillis()-maxViewSIData.getTime())/1000L/60L > Constant.staleThreshold){
                itcStatus.setDeviceStatus(Constant.TOPSERVER_ITC_OFF);
            }
            itcStatus.setDeviceStatus(Constant.TOPSERVER_ITC_ON);
            res.add(itcStatus);
        }
        return res;
    }

    @Override
    public List<CallRecord> getCallRecords()  {
        CacheStructQueue q = CacheStructQueue.getBlockingQueue();
        List<CallRecord> res = new ArrayList<>();
        while(q.size() != 0){
            log.info("the blocking queue has "+ q.size()+ " elements");
            MaxViewCallRecord record = null;
            try {
                 record = q.take();
                //todo need to map fromid to object id
                CallRecord r = new CallRecord();
                String callSip = record.getFromTid();
                String beCallSip = record.getToIid();
                String callDevice = "13"+callSip+"60"+callSip+callSip;
                String beCallDevice = "13"+beCallSip+"60"+beCallSip+beCallSip;
                r.setCallDevice(callDevice);
                r.setCallSip(callSip);
                r.setBecallDevice(beCallDevice);
                r.setBecallSip(beCallSip);
                r.setStartTime(record.getStartTime());
                r.setAnswerTime(record.getStartTime());
                String startTime = r.getStartTime();
                try {


                    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    LocalDateTime time = LocalDateTime.parse(startTime, dtf);
                    LocalDateTime endTime = time.plusSeconds(record.getSeconds());
                    r.setStatus(Utils.tranlatePhoneStatus(record.getStatus()));
                    Date et = Date.from(endTime.atZone(Constant.DEFAULT_ZONE).toInstant());

                    r.setEndTime(et);
                }catch (Exception e){
                    log.error(e.getMessage());
                    log.error("fail to parse date time");
                }
                res.add(r);
            }catch (Exception e){
                log.error(e.getMessage());
                log.error("fail to pop out element..." );
            }
        }
        return res;
    }

    @Override
    public List<FireDeviceStatus> getAlarms() {
        CacheAlarmMap mp = CacheAlarmMap.getCacheMap();
        List<FireDeviceStatus> res = new ArrayList<>();
        for(String id : Constant.FIRE_OBJ_SET){
            LocalDateTime time = LocalDateTime.now();
            FireDeviceStatus device = new FireDeviceStatus();
            if(mp.contains(id) && (mp.get(id).getCleared().equals("false")||mp.get(id).getCleared()==null)){
                device.setDeviceStatus("1");
            }else{
                //if we are in cleared state
                if(mp.contains(id) && mp.get(id).getCleared().equals("true")) mp.remove(id);
                device.setDeviceStatus("0");
            }
            device.setDeviceCode(id);
            device.setMessage(mp.get(id).getAdditionalText());
            Date t = Date.from(time.atZone(Constant.DEFAULT_ZONE).toInstant());
            device.setMonitorTime(t);
            res.add(device);
        }
        return res;
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


        long curTime = Instant.now().getEpochSecond();
        DeviceStatus deviceStatus = new DeviceStatus();
        Long  switchStatusArriveTime = maxViewDIStatus.getSwitchStatusArriveTime();


        if(switchStatusArriveTime != null && (curTime-switchStatusArriveTime/1000L)/60L >Constant.staleThreshold){
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

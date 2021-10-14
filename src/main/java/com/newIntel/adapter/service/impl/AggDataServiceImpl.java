package com.newIntel.adapter.service.impl;

import com.newIntel.adapter.Cache.CacheAIMap;
import com.newIntel.adapter.Cache.CacheDIMap;
import com.newIntel.adapter.Cache.CacheSIMap;
import com.newIntel.adapter.Cache.CacheStructQueue;
import com.newIntel.adapter.bean.*;
import com.newIntel.adapter.constant.Constant;
import com.newIntel.adapter.service.AggDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class AggDataServiceImpl implements AggDataService {
    private static final Logger log = LoggerFactory.getLogger(AggDataServiceImpl.class);
    private static final byte[] AIlock = new byte[0];

    @Override
    public void aggAIdata(List<MaxViewAIData> mpList) {
        if (mpList.size() == 0) {
            return;
        }

        for (MaxViewAIData data : mpList) {
            String objectID = data.getObjectId();
            //前四位+后四位
            String locationID = objectID.substring(0, 4) + objectID.substring(6);

            String deviceType = objectID.substring(4, 6);


            String[] typeList = {Constant.CO_TYPE_CODE, Constant.CH4_TYPE_CODE, Constant.H2S_TYPE_CODE, Constant.O2_TYPE_CODE, Constant.TEMP_TYPE_CODE, Constant.HUMIDITY_TYPE_CODE};
            if (!Arrays.asList(typeList).contains(deviceType)) {
                log.info("===============device type can not be matched.......... object id is " + objectID);
                continue;
            }


            synchronized (AIlock) {
                CacheAIMap mp = CacheAIMap.getCacheMap();
                if (!mp.containsKey(locationID)) {
                    AiStatus aiStatus = new AiStatus();
                    aiStatus = updateAiStatus(data, aiStatus);
                    //init devicemac as locationid
                    aiStatus.setDeviceMac(locationID);
                    mp.put(locationID, aiStatus);
                } else {
                    AiStatus aiStatus = mp.get(locationID);
                    aiStatus = updateAiStatus(data, aiStatus);
                    mp.put(locationID, aiStatus);
                }
            }

        }

    }

    @Override
    public void aggDIdata(List<MaxViewAIData> mpList) {
        if (mpList.size() == 0) {
            return;
        }

        for (MaxViewAIData data : mpList) {
            String objectID = data.getObjectId();

            String locationID = objectID;
            String deviceType = objectID.substring(4, 6);

            /*
            String[] typeList = {Constant.WATERPUMP_OUTPUT_TYPE_CODE};
            if (!Arrays.asList(typeList).contains(deviceType)) {
                log.info("===============SWITCH type can not be matched.......... object id is " + objectID);
                continue;
            }
            */
            CacheDIMap mp = CacheDIMap.getCacheMap();
            if (!mp.containsKey(locationID)) {
                MaxViewDIStatus maxViewDiStatus = new MaxViewDIStatus();
                maxViewDiStatus = updateDiStatus(data, maxViewDiStatus);
                mp.put(locationID, maxViewDiStatus);
            } else {
                MaxViewDIStatus maxViewDiStatus = mp.get(locationID);
                maxViewDiStatus = updateDiStatus(data, maxViewDiStatus);
                mp.put(locationID, maxViewDiStatus);
            }

        }
    }

    @Override
    public void aggSIdata(List<MaxViewSIData> mpList) {
        CacheSIMap mp = CacheSIMap.getCacheMap();
        for (MaxViewSIData data : mpList) {
            String objID = data.getObjectId();
            if(objID == null) continue;
            if(Constant.PHOPNE_OBJ_SET.contains(objID)){
                mp.put(data.getObjectId(),data);
                data.setTime(System.currentTimeMillis());
                mp.put(objID, data);
            }

        }
    }

    @Override
    public void aggStructData(List<MaxViewCallRecord> mplist) {
        CacheStructQueue q = CacheStructQueue.getBlockingQueue();
        for(MaxViewCallRecord record : mplist){
            q.add(record);
        }
    }

    //todo
    // convert cv format,
    // verify if it is outdated
    // /*
    //     //CO：;整数型.
    //    public static final int CO_TYPE_CODE = 11;
    //    //CH4：;整数型
    //    public static final int CH4_TYPE_CODE = 12;
    //    //H2S：;整数型
    //    public static final int H2S_TYPE_CODE = 13;
    //    //O2：;氧气浓度,精确到小数点后两位
    //    public static final int O2_TYPE_CODE = 14;
    //    //环境温度：;精确到小数点后两位
    //    public static final int TEMP_TYPE_CODE = 15;
    //    //环境湿度：;精确到小数点后两位
    //    public static final int HUMIDITY_TYPE_CODE = 16;/
    //
    //*/*/
    private AiStatus updateAiStatus(MaxViewAIData data, AiStatus aiStatus) {
        String objectID = data.getObjectId();
        String deviceType = objectID.substring(4, 6);
        String cv = data.getCv();
        Long time = data.getTime();
        if(time == null || String.valueOf(time).length() != 13 ){
            log.info("no time passed into , use current timestamp");
            time = System.currentTimeMillis();
        }

        switch (deviceType) {
            case Constant.CO_TYPE_CODE:
                aiStatus.setCarbonMonoxide(convertCVInt(cv));
                aiStatus.setCarbonMonoxideArriveTime(time);
                break;
            case Constant.CH4_TYPE_CODE:
                aiStatus.setMethane(convertCVInt(cv));
                aiStatus.setMethaneArriveTime(time);
                break;
            case Constant.H2S_TYPE_CODE:
                aiStatus.setHydrogenSulfide(convertCVInt(cv));
                aiStatus.setHydrogenSulfideArriveTime(time);
                break;
            case Constant.O2_TYPE_CODE:
                aiStatus.setOxygen(convertCV2Dig(cv));
                aiStatus.setOxygenArriveTime(time);
                break;
            case Constant.TEMP_TYPE_CODE:
                aiStatus.setTemperature(convertCV2Dig(cv));
                aiStatus.setTemperatureArriveTime(time);
                break;
            case Constant.HUMIDITY_TYPE_CODE:
                aiStatus.setHumidity(convertCV2Dig(cv));
                aiStatus.setHumidityArriveTime(time);
                break;
            case Constant.WATERPUMP_OUTPUT_TYPE_CODE:
                aiStatus.setWaterPumpStatus(cv);
                aiStatus.setHumidityArriveTime(time);
                break;
        }

        return aiStatus;
    }

    private String convertCVInt(String s) {
        String res = "";
        float f;
        try {
            f = Float.parseFloat(s);
            int i = (int) f;
            res = String.valueOf(i);
        } catch (Exception e) {
            log.error(e.toString());
            log.error("incoming string is " + s, " not a legal value,unable to convert to int");
        }

        return res;
    }

    private String convertCV2Dig(String s) {
        String res = "";
        float f;
        try {
            f = Float.parseFloat(s);

            res = String.format("%.02f", f);
        } catch (Exception e) {
            log.error(e.toString());
            log.error("incoming string is " + s, " not a legal value, unable to convert to decimal");
        }

        return res;
    }

    private String convertSwitchCV(String cv) {
        if (cv.equals("true")) {
            return Constant.DEVICE_ON;
        }
        return Constant.DEVICE_CLOSE;
    }

    // update DI status
    private MaxViewDIStatus updateDiStatus(MaxViewAIData data, MaxViewDIStatus maxViewDiStatus) {

        maxViewDiStatus.setSwitchStatus(Constant.DEVICE_OFFLINE);
        if (data.getCv().equals(Constant.MAXIEW_SWITCH_STATUS_ON)) {
            maxViewDiStatus.setSwitchStatus(Constant.DEVICE_ON);
        }

        if (data.getCv().equals(Constant.MAXIEW_SWITCH_STATUS_OFF)) {
            maxViewDiStatus.setSwitchStatus(Constant.DEVICE_CLOSE);
        }
        maxViewDiStatus.setSwitchStatusArriveTime(data.getTime());
        return maxViewDiStatus;
    }
}


//If(data.getCv().equals(Constant.MAXIEW_SWITCH_STATUS_ON){
    /*
    * if(deviceType.equals(Constant.CO_TYPE_CODE)){
            AiStatus
        }else if(deviceType.equals(Constant.CH4_TYPE_CODE)){

        }else if(deviceType.equals(Constant.H2S_TYPE_CODE)){

        }else if(deviceType.equals(Constant.O2_TYPE_CODE)){

        }else if(deviceType.equals(Constant.TEMP_TYPE_CODE)){

        }else if(deviceType.equals(Constant.HUMIDITY_TYPE_CODE)){

        }*
          //
    //CO：;整数型.
    public static final int CO_TYPE_CODE = 11;
    //CH4：;整数型
    public static final int CH4_TYPE_CODE = 12;
    //H2S：;整数型
    public static final int H2S_TYPE_CODE = 13;
    //O2：;氧气浓度,精确到小数点后两位
    public static final int O2_TYPE_CODE = 14;
    //环境温度：;精确到小数点后两位
    public static final int TEMP_TYPE_CODE = 15;
    //环境湿度：;精确到小数点后两位
    public static final int HUMIDITY_TYPE_CODE = 16;/

*/

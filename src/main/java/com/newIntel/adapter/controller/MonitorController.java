package com.newIntel.adapter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newIntel.adapter.Cache.CacheAlarmMap;
import com.newIntel.adapter.Cache.CacheSIMap;
import com.newIntel.adapter.bean.*;
import com.newIntel.adapter.config.SysConfComponent;
import com.newIntel.adapter.constant.Constant;
import com.newIntel.adapter.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


//@Controller
//@ResponseBody
@RestController
public class MonitorController {
    private static final Logger log = LoggerFactory.getLogger(MonitorController.class);
    @Autowired
    private SysConfComponent sysConfComponent;

    @Autowired
    private MaxViewSwitchService maxViewSwitchService;

    @Autowired
    private AggDataService aggDataService;

    @Autowired
    private GetCacheDataService getCacheDataService;

    @Autowired
    private ControlMaxViewService controlMaxViewService;

    @Autowired
    private PushData2TopService pushData2TopService;



    @GetMapping("/test")
    List<AiStatus> getAiData() throws Exception {
        log.info("try to get some AIdata");
        log.info("device status is" + getCacheDataService.getDeviceStatus() );
        //pushData2TopService.sendDeviceData();
        return getCacheDataService.getAiData();
    }

    @GetMapping("/test2")
    void getAiData1() throws Exception {

        pushData2TopService.sendFireDeviceStatus();
        //pushData2TopService.sendDeviceData();
    }

    @GetMapping("/test3")
    void getAiData2() throws Exception {

        pushData2TopService.sendCallRecords();
        //pushData2TopService.sendDeviceData();
    }

    @GetMapping("/test4")
    void getAiData4() throws Exception {

        pushData2TopService.sendITCStatus();
        //pushData2TopService.sendDeviceData();
    }

    //api/v1/measvalue-ai
    @PostMapping("/api/v1/measvalue-ai")
    AjaxResult pushAIData(@RequestBody List<MaxViewAIData> mpList) {
        log.info("try to get AI value:"+String.valueOf(mpList));
         aggDataService.aggAIdata(mpList);
         return AjaxResult.success("ok");

    }


    @PostMapping("/api/v1/control")
    TopServerControlResult controlDevice(@RequestBody TopServerDoData topServerDoData) {
        TopServerControlResult res = new TopServerControlResult();
        res.setContrrolResult(topServerDoData.getControl());
        res.setDeviceMac(topServerDoData.getDeviceMac());

        try {
            controlMaxViewService.controlSwitch(topServerDoData);
        } catch (Exception e) {
            res.setContrrolResult(Constant.DEVICE_OFFLINE);
        }
        return res;

    }

    @PostMapping("/api/v1/measvalue-si")
    AjaxResult pushSiData(@RequestBody List<MaxViewSIData> mpList){
        log.info("try to get SI value:"+String.valueOf(mpList));
        aggDataService.aggSIdata(mpList);
        return AjaxResult.success("ok");
    }

    @PostMapping("/api/v1/measvalue-di")
    AjaxResult pushDIData(@RequestBody List<MaxViewAIData> mpList){
        //TODO SEND MAXVIEW DATA TO UPSIDE SERVER
        log.info("try to get DI value:"+String.valueOf(mpList));
        aggDataService.aggDIdata(mpList);
        return AjaxResult.success("ok");
    }

    @PostMapping("api/v1/new-alarms")
    AjaxResult pushNewAlarmData(@RequestBody List<MaxViewAlarm> mpList){
        log.info("try to get new Alarm:"+String.valueOf(mpList));
        CacheAlarmMap mp = CacheAlarmMap.getCacheMap();
        for(MaxViewAlarm alarm:mpList){
           String objId = alarm.getObjectId();
           if(!Constant.FIRE_OBJ_SET.contains(objId)) continue;
           mp.put(objId,alarm);
        }
        return AjaxResult.success("ok");
    }

    @PostMapping("api/v1/cleared-alarms")
    AjaxResult pushUpatedAlarmData(@RequestBody List<MaxViewAlarm> mpList){
        log.info("try to get cleared Alarm:"+String.valueOf(mpList));
        CacheAlarmMap mp = CacheAlarmMap.getCacheMap();
        for(MaxViewAlarm alarm:mpList){
            String objId = alarm.getObjectId();
            if(!Constant.FIRE_OBJ_SET.contains(objId)) continue;
            mp.put(objId,alarm);
        }
        return AjaxResult.success("ok");
    }

    @PostMapping("/api/v1/structure-obj")
    AjaxResult pushStructData(@RequestBody List<List<Map<String,Object>>> mpList){
        log.info("try to get struct object:"+String.valueOf(mpList));
        List<MaxViewCallRecord> resList = new ArrayList<>();
        for(List<Map<String,Object>> l : mpList){
            if(l==null || l.size() ==0) continue;
            String objid = (String) l.get(0).get("objectId");//
            //提取电话记录
            //todo get objid
            String obj = sysConfComponent.getPhoneCallObjId();
            if(!objid.equals(obj)) continue;
            for(Map<String,Object> mp : l){
                ObjectMapper mapper = new ObjectMapper();
                MaxViewCallRecord record = mapper.convertValue(mp.get("cv"), MaxViewCallRecord.class);
                resList.add(record);
            }
            aggDataService.aggStructData(resList);
        }
        return AjaxResult.success("ok");
    }
}


//https://stackoverflow.com/questions/43997857/post-a-json-array-into-spring-boot-restcontroller
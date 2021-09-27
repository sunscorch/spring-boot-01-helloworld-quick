package com.newIntel.adapter.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newIntel.adapter.Cache.CacheDIMap;
import com.newIntel.adapter.bean.MaxViewDIStatus;
import com.newIntel.adapter.bean.MaxViewDoData;
import com.newIntel.adapter.bean.TopServerDoData;
import com.newIntel.adapter.config.SysConfComponent;
import com.newIntel.adapter.constant.Constant;
import com.newIntel.adapter.service.ControlMaxViewService;
import com.newIntel.adapter.service.GetCacheDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
@Service
public class ControlMaxViewServiceImpl implements ControlMaxViewService {
    private static final Logger log = LoggerFactory.getLogger(MaxViewSwitchServiceImpl.class);

    @Autowired
    private SysConfComponent sysConfComponent;



    @Override
    public void controlSwitch(TopServerDoData topServerDoData) throws Exception {
        String deviceMac = topServerDoData.getDeviceMac();
        String control = topServerDoData.getControl();
        MaxViewDoData maxViewDoData = new MaxViewDoData();

        maxViewDoData.setObjectId(topServerDoData.getDeviceMac());

        if(control.equals(Constant.DEVICE_ON)){
            maxViewDoData.setCv(Constant.MAXIEW_SWITCH_STATUS_ON);
        }else if (control.equals(Constant.DEVICE_CLOSE)){
            maxViewDoData.setCv(Constant.MAXIEW_SWITCH_STATUS_OFF);
        }else{
            throw new Exception("invaild control status" + "request data is +" + topServerDoData.toString());

        }

        if(!checkSwitchStatus(topServerDoData.getDeviceMac())){
            throw new Exception("the switch is offline , fail to send request");
        }


        RestTemplate restTemplate = new RestTemplate();
        String url = "http://" + sysConfComponent.getMaxViewIP()+":"+  sysConfComponent.getMaxViewPort()+"/api/v1/measvalue-do/";
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);

        ObjectMapper mapper = new ObjectMapper();
        List<MaxViewDoData> l = new ArrayList<>();
        l.add(maxViewDoData);
        String jsonString = mapper.writeValueAsString(l);

        log.info("send to maxview so data :"+ jsonString);
        HttpEntity<String> entity = new HttpEntity<String>(jsonString,headers);
        String result = restTemplate.postForObject(url, entity, String.class);
        //todo verify  json foramt

        if (null != result) {
            throw new Exception("maxview fail to update the date");

        }

        log.info("send to maxview successfully");


    }

    private  Boolean checkSwitchStatus(String objectid){
        String deviceID = objectid.substring(4,6);
        Boolean res = true;
        long staleThreshold = 5L;
        long curTime = Instant.now().getEpochSecond();
        if(deviceID.equals(Constant.WATERPUMP_INPUT_TYPE_CODE)){
            String outputObjId = objectid.substring(0,4)+Constant.WATERPUMP_OUTPUT_TYPE_CODE+objectid.substring(6);
            CacheDIMap mp = CacheDIMap.getCacheMap();
            MaxViewDIStatus status = mp.get(outputObjId);
            if(status == null) {
                res =false;
                log.error("switch status is not uploaded ");
            }
            if(status.getSwitchStatusArriveTime() != null && (curTime-status.getSwitchStatusArriveTime() /1000L)/60L >staleThreshold){
                res =false;
                log.error("device is off line or its status is not uploaded ");
            }
        }

        return res;
    }
}

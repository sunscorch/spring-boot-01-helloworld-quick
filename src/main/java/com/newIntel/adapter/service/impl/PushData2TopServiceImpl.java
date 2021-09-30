package com.newIntel.adapter.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.newIntel.adapter.Cache.CacheStructQueue;
import com.newIntel.adapter.bean.*;
import com.newIntel.adapter.config.SysConfComponent;
import com.newIntel.adapter.service.GetCacheDataService;
import com.newIntel.adapter.service.PushData2TopService;
import com.newIntel.adapter.utils.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class PushData2TopServiceImpl implements PushData2TopService {
    private static final Logger log = LoggerFactory.getLogger(PushData2TopServiceImpl.class);

    @Autowired
    private GetCacheDataService getCacheDataService;

    @Autowired
    private SysConfComponent sysConfComponent;

    @Qualifier("customizeObjectMapper")
    @Autowired
    ObjectMapper mapper;

    @Override
    public void sendAiData() throws Exception{
       List<AiStatus> dataList =  getCacheDataService.getAiData();
        RestTemplate restTemplate = new RestTemplate();

        String url = sysConfComponent.getTopServerAiUrl();

        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);

        //ObjectMapper mapper = new ObjectMapper();

        String jsonString ;
        try{
            jsonString = mapper.writeValueAsString(dataList);
        }catch(Exception e){
            log.error(e.getMessage());
            log.error("failed json is" + dataList.toString());
            throw e;
        }

        log.info("send to top server AI data : "+ jsonString);
        HttpEntity<String> entity = new HttpEntity<String>(jsonString,headers);
        String result = restTemplate.postForObject(url, entity, String.class);
        log.info("finish sending to top server get request result :" + result);

    }

    @Override
    public void sendDeviceData() throws Exception {
        List<DeviceStatus> dataList =  getCacheDataService.getDeviceStatus();

        RestTemplate restTemplate = new RestTemplate();

        String url = sysConfComponent.getTopServerDeviceUrl();

        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);

        ObjectMapper mapper = new ObjectMapper();

        String jsonString ;
        try{
            jsonString = mapper.writeValueAsString(dataList);
        }catch(Exception e){
            log.error(e.getMessage());
            log.error("failed json is" + dataList.toString());
            throw e;
        }

        log.info("send to top server device data : "+ jsonString);
        HttpEntity<String> entity = new HttpEntity<String>(jsonString,headers);
        String result = restTemplate.postForObject(url, entity, String.class);
        log.info("finish sending to top server get request result :" + result);
    }

    @Override
    public void sendITCStatus() {
        List<ItcStatus> itcStatusList = getCacheDataService.getItcStatus();
        if(itcStatusList == null || itcStatusList.size()==0){
            log.info("there is no itcStatusList  now, break the send request");
            return;
        }

        RestTemplate restTemplate = new RestTemplate();

        String url = sysConfComponent.getTopServerPhoneUrl();

        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);

        String jsonString ;
        ObjectMapper mapper = new ObjectMapper();
        try{
            ObjectNode obj = mapper.createObjectNode();
            obj.put("event","PUSH_DEVICE_STATUS");
            obj.putPOJO("data",itcStatusList);
            jsonString = obj.toString();
        }catch(Exception e){
            log.error(e.getMessage());
            log.error("failed json is" + itcStatusList.toString());
            throw e;
        }

        log.info("send to top server itc status data : "+ jsonString);
        HttpEntity<String> entity = new HttpEntity<String>(jsonString,headers);
        String result = restTemplate.postForObject(url, entity, String.class);
        log.info("finish sending to top server get request result :" + result);

    }



    @Override
    public void sendCallRecords() throws Exception {
       List<CallRecord> res = getCacheDataService.getCallRecords();

       if(res == null || res.size()==0){
           log.info("there is no call records now, brake the send request");
           return;
       }
        RestTemplate restTemplate = new RestTemplate();

        String url = sysConfComponent.getTopServerPhoneUrl();

        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);

        String jsonString ;
        ObjectMapper mapper = new ObjectMapper();
        try{
            ObjectNode obj = mapper.createObjectNode();
            obj.put("event","PUSH_TALK_RECORD");
            obj.putPOJO("data",res);
            jsonString = obj.toString();
        }catch(Exception e){
            log.error(e.getMessage());
            log.error("failed json is" + res.toString());
            throw e;
        }

        log.info("send to top server call record data : "+ jsonString);
        HttpEntity<String> entity = new HttpEntity<String>(jsonString,headers);
        String result = restTemplate.postForObject(url, entity, String.class);
        log.info("finish sending to top server get request result :" + result);

    }

    @Override
    public void sendFireDeviceStatus() throws Exception {
        List<FireDeviceStatus> dataList = getCacheDataService.getAlarms();

        if(dataList == null || dataList.size()==0){
            log.info("there is no fire device now, break the send request");
            return;
        }

        RestTemplate restTemplate = new RestTemplate();

        String url = sysConfComponent.getTopServerFireAlarmUrl();

        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);

        ObjectMapper mapper = new ObjectMapper();

        String jsonString ;
        try{
            jsonString = mapper.writeValueAsString(dataList);
        }catch(Exception e){
            log.error(e.getMessage());
            log.error("failed json is" + dataList.toString());
            throw e;
        }

        log.info("send to top server fire device data : "+ jsonString);
        HttpEntity<String> entity = new HttpEntity<String>(jsonString,headers);
        String result = restTemplate.postForObject(url, entity, String.class);
        log.info("finish sending to top server  result :" + result);

    }
}

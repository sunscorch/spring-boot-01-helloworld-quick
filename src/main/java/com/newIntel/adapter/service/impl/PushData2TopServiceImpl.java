package com.newIntel.adapter.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newIntel.adapter.bean.AiStatus;
import com.newIntel.adapter.bean.DeviceStatus;
import com.newIntel.adapter.config.SysConfComponent;
import com.newIntel.adapter.service.GetCacheDataService;
import com.newIntel.adapter.service.PushData2TopService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

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
}

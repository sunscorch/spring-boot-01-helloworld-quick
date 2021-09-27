package com.newIntel.adapter.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newIntel.adapter.config.SysConfComponent;
import com.newIntel.adapter.service.MaxViewSwitchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class MaxViewSwitchServiceImpl implements MaxViewSwitchService {
    private static final Logger log = LoggerFactory.getLogger(MaxViewSwitchServiceImpl.class);

    @Autowired
    private SysConfComponent sysConfComponent;

    @Override
    public Boolean controlSwitch(List<Map<String,Object>> mpList) throws Exception{
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://" + sysConfComponent.getMaxViewIP()+":"+  sysConfComponent.getMaxViewPort()+"/api/v1/measvalue-do/";
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("application/json; charset=UTF-8");
        headers.setContentType(type);

        ObjectMapper mapper = new ObjectMapper();

        String jsonString = mapper.writeValueAsString(mpList);

        log.info("send to maxview so data :"+ jsonString);
        HttpEntity<String> entity = new HttpEntity<String>(jsonString,headers);
        String result = restTemplate.postForObject(url, entity, String.class);
        //todo verify  json foramt

        if (null != result) {
            throw new Exception(result);

        }
        //todo verify switch status
        return true;
    }

}

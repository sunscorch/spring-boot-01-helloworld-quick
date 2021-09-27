package com.newIntel.adapter.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;


import java.util.HashMap;
import java.util.Map;

@Component

public class InitApplicationRunner implements CommandLineRunner {
    private static final Logger log = LoggerFactory.getLogger(InitApplicationRunner.class);
    @Autowired
    private SysConfComponent sysConfComponent;
    @Override
    public void run(String... args) throws Exception {
        log.info("=======================Monitor app is going to init config=======================");

        //No neen to login now
        //getToken();


    }

    private void getToken(){
        try{
            String ip = sysConfComponent.getMaxViewIP();
            String port = sysConfComponent.getMaxViewPort();
            //TODO ADD string to const
            String url = "http://"+ip+":"+port+"/api/vi/login";
            log.info("=======================maxView's url is:" + url);
           // JSONObject object = new JSONObject();
            ObjectMapper mapper = new ObjectMapper();
            Map<String, String> map = new HashMap<>();
            map.put("user",sysConfComponent.getMaxViewUser());
            map.put("pwd",sysConfComponent.maxViewPwd);

            String jsonString = mapper.writeValueAsString(map);
            log.info("================my json is "+ jsonString);
        }catch (Exception e){
            log.error(e.toString());
        }
    }

}

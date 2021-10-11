package com.newIntel.adapter.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//https://segmentfault.com/a/1190000018805591
@Component
public class CronService {
    @Autowired
    PushData2TopService pushData2TopService;

    @Scheduled(fixedDelayString = "${scheduled.fixedDelay}",initialDelayString = "${scheduled.initialDelay}"  )
    public void sendAIData2TopServer() throws Exception{
        pushData2TopService.sendAiData();
        //pushData2TopService.sendDeviceData();
    }

    @Scheduled(fixedDelayString = "${scheduled.fixedDelay}",initialDelayString = "${scheduled.initialDelay}"  )
    public void sendItcStatus2TopServer() throws Exception{
        pushData2TopService.sendITCStatus();
    }

    @Scheduled(fixedDelayString = "${scheduled.fixedDelay}",initialDelayString = "${scheduled.initialDelay}"  )
    public void sendCallRecord2TopServer() throws Exception{
        pushData2TopService.sendCallRecords();
    }

    //火灾告警
    @Scheduled(fixedDelayString = "${scheduled.fixedDelay}",initialDelayString = "${scheduled.initialDelay}"  )
    public void sendFireDeviceStatus2TopServer() throws Exception{
        pushData2TopService.sendFireDeviceStatus();
    }
}

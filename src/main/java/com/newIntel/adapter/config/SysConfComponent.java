package com.newIntel.adapter.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "sysconf")
public class SysConfComponent
{
    String maxViewtoken = null;
    String maxViewIP = null;
    String maxViewPort = null;


    String maxViewUser = null;
    String maxViewPwd = null;

    String TopServerAiUrl = null;

    String TopServerDeviceUrl = null;

    String TopServerPhoneUrl = null;

    String TopServerFireAlarmUrl = null;

    String PhoneCallObjId = null;

}
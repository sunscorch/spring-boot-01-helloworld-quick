package com.newIntel.adapter.bean;

import lombok.Data;
import com.fasterxml.jackson.annotation.*;


@Data
public class DeviceStatus {
    private String deviceMac;
    private String waterPumpStatus;
    @JsonIgnore
    private  Long  waterPumpStatusArriveTime;

    private String fanStatus;
    @JsonIgnore
    private  Long  fanStatusArriveTime;

    private String dataTime;
}

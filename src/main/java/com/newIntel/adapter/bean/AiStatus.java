package com.newIntel.adapter.bean;
import com.newIntel.adapter.constant.Constant;
import lombok.Data;
import com.fasterxml.jackson.annotation.*;


@Data
@JsonFilter("myFilter")
public class AiStatus {
    // 温度,精确到小数点后两位
    private  String temperature;
    @JsonIgnore
    private  Long temperatureArriveTime;

    // 湿度,精确到小数点后两位
    private  String humidity;
    @JsonIgnore
    private  Long  humidityArriveTime;

    // 氧气浓度,精确到小数点后两位
    private  String oxygen;
    @JsonIgnore
    private  Long  oxygenArriveTime;

    // 一氧化碳浓度,整数型.
    private String carbonMonoxide;
    @JsonIgnore
    private  Long  carbonMonoxideArriveTime;

    // 硫化氢,整数型
    private String hydrogenSulfide;
    @JsonIgnore
    private  Long  hydrogenSulfideArriveTime;

    // 甲烷,整数型
    private String methane;
    @JsonIgnore
    private  Long  methaneArriveTime;


    private String waterPumpStatus;
    @JsonIgnore
    private  Long  waterPumpStatusArriveTime;

    @JsonIgnore
    private String fanStatus;

    private String deviceMac;

    private String dataTime;


}

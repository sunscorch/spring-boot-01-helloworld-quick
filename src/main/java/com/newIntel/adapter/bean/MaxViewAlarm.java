package com.newIntel.adapter.bean;

import lombok.Data;

@Data
public class MaxViewAlarm {

    private String objectId;
    private long time;
    private String additionalText;
    private String cleared;
    private String alarmName;
}

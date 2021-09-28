package com.newIntel.adapter.bean;

import lombok.Data;

@Data
public class MaxViewCallRecord {
    private String fromTid;
    private String toIid;
    private String from;
    private String to;
    private String type;
    private int status;
    private String startTime;
    private int seconds;
}

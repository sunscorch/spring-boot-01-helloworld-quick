package com.newIntel.adapter.bean;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Date;


@Data
public class CallRecord {
    private String callDevice;
    private String callSip;
    private String becallDevice;
    private String becallSip;
    private String startTime;

    private String answerTime;

    @JsonFormat(shape = JsonFormat.Shape.STRING,pattern = "yyyy-MM-dd HH:mm:ss")
    private Date endTime;
    private String status;

}

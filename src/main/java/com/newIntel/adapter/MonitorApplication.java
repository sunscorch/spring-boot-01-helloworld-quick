package com.newIntel.adapter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;

//@EnableScheduling
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class MonitorApplication {

    public static void main(String[] args) {
        SpringApplication.run(MonitorApplication.class, args);
    }

}


/*
*
*
  @echo off
  start javaw -jar demo.jar --spring.config.location=application.properties
  exit

=======
@echo off
taskkill -f -t -im javaw.exe
exit
*
*
*
* *
taskkill /pid 2552 -f
* */

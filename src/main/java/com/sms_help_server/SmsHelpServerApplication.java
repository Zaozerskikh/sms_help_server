package com.sms_help_server;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;


@EnableAsync
@SpringBootApplication
public class SmsHelpServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SmsHelpServerApplication.class, args);
    }

}

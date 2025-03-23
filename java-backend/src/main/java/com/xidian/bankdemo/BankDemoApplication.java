package com.xidian.bankdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class BankDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(BankDemoApplication.class, args);
    }

}

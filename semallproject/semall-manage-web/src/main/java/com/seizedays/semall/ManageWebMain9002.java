package com.seizedays.semall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class ManageWebMain9002 {
    public static void main(String[] args) {
        SpringApplication.run(ManageWebMain9002.class, args);
    }
}

package com.seizedays.semall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages="com.seizedays.semall.manage.mappers")
public class ManageServiceMain8002 {
    public static void main(String[] args) {
        SpringApplication.run(ManageServiceMain8002.class, args);
    }
}

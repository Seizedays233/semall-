package com.seizedays.semall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages="com.seizedays.semall.order.mappers")
public class OrderServiceMain8042 {
    public static void main(String[] args) {
        SpringApplication.run(OrderServiceMain8042.class, args);
    }
}

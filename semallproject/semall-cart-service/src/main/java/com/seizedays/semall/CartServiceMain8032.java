package com.seizedays.semall;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages="com.seizedays.semall.cart.mappers")
public class CartServiceMain8032 {
    public static void main(String[] args) {
        SpringApplication.run(CartServiceMain8032.class, args);
    }
}

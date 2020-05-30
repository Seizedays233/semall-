package com.seizedays.semall.search;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan(basePackages="com.seizedays.semall.search.mappers")
public class SearchServiceMain8022 {
    public static void main(String[] args) {
        SpringApplication.run(SearchServiceMain8022.class, args);
    }
}

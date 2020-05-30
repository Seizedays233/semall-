package com.seizedays.semall.passport;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages="com.seizedays.semall.user.mapper")
public class SemallUserApplication {

    public static void main(String[] args) {
        SpringApplication.run(SemallUserApplication.class, args);
    }

}

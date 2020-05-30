package com.seizedays.semall.passport.services;


import com.seizedays.semall.beans.UmsMember;
import com.seizedays.semall.webutils.configs.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "semall-user-service",configuration = FeignConfig.class)
public interface PassPortService {

    @RequestMapping("/userService/login")
    UmsMember login(@RequestBody UmsMember umsMember);

    @RequestMapping("/addUserToken")
    void addUserToken(@RequestParam("token") String token, @RequestParam("memberId") Long memberId);

    @RequestMapping("/userService/addUser")
    void addOauthUser(@RequestBody UmsMember umsMember);
}

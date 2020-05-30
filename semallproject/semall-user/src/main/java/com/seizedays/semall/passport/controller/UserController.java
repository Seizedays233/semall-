package com.seizedays.semall.passport.controller;

import com.seizedays.semall.beans.UmsMember;
import com.seizedays.semall.beans.UmsMemberReceiveAddress;
import com.seizedays.semall.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping("index")
    @ResponseBody
    public String index(){
        return "hello user";
    }

    @RequestMapping("getAllUSer")
    @ResponseBody
    public List<UmsMember> getAllUser(){

        List<UmsMember> umsMembers =  userService.getAllUSer();

        return umsMembers;
    }

    @RequestMapping("getReceiveAddress")
    @ResponseBody
    public List<UmsMemberReceiveAddress> getReceiveAddressByNumberId(Long memberId){

        return userService.getReceiveAddressByMemberId(memberId);
    }
}

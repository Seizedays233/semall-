package com.seizedays.semall.passport.controller;

import com.seizedays.semall.beans.UmsMember;
import com.seizedays.semall.beans.UmsMemberReceiveAddress;
import com.seizedays.semall.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@CrossOrigin
public class UserController {

    @Autowired
    UserService userService;


    @RequestMapping("/userService/login")
    @ResponseBody
    public UmsMember login(@RequestBody UmsMember umsMember) {
        return userService.login(umsMember);
    }

    @RequestMapping("/addUserToken")
    @ResponseBody
    public void addUserToken(@RequestParam("token") String token, @RequestParam("memberId") Long memberId) {
        userService.addUserToken(token, memberId);
    }

    @RequestMapping("/userService/addUser")
    @ResponseBody
    public void addOauthUser(@RequestBody UmsMember umsMember) {
        Integer sourceType = umsMember.getSourceType();
        UmsMember umsMemberCheck = null;
        if (null != sourceType && sourceType == 2) {
            umsMemberCheck = userService.getUserBySourceUid(umsMember.getSourceUid());
        }

        if (null == umsMemberCheck) {
            userService.addUser(umsMember);
        }

    }


    @RequestMapping("/getUser")
    @ResponseBody
    public List<UmsMember> getAllUser() {

        List<UmsMember> umsMembers = userService.getAllUSer();

        return umsMembers;
    }

    @RequestMapping("/getReceiveAddress")
    @ResponseBody
    public List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(Long memberId) {

        return userService.getReceiveAddressByMemberId(memberId);
    }

    @RequestMapping("/getReceiveAddressByAddrId")
    @ResponseBody
    UmsMemberReceiveAddress getReceiveAddressByAddressId(@RequestParam("receiveAddressId") String receiveAddressId){
        return  userService.getReceiveAddressByAddressId(receiveAddressId);
    }
}

package com.seizedays.semall.order.services;

import com.seizedays.semall.beans.UmsMemberReceiveAddress;
import com.seizedays.semall.webutils.configs.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@FeignClient(value = "semall-user-service",configuration = FeignConfig.class)
public interface ServiceForUser {

    @RequestMapping("/getReceiveAddress")
    List<UmsMemberReceiveAddress> getReceiveAddressByMemberId(@RequestParam("memberId") Long memberId);

    @RequestMapping("/getReceiveAddressByAddrId")
    UmsMemberReceiveAddress getReceiveAddressByAddressId(@RequestParam("receiveAddressId") String receiveAddressId);
}

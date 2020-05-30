package com.seizedays.semall.payment.services;

import com.seizedays.semall.beans.OmsOrder;
import com.seizedays.semall.webutils.configs.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "semall-order-service",configuration = FeignConfig.class)
public interface OrderServiceForPayment {

    @RequestMapping("/getOrderByOutTradeNo")
    OmsOrder getOrderByOutTradeNo(@RequestParam("outTradeNo") String outTradeNo);
}

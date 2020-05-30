package com.seizedays.semall.order.services;

import com.seizedays.semall.beans.OmsOrder;
import com.seizedays.semall.webutils.configs.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;

@FeignClient(value = "semall-order-service",configuration = FeignConfig.class)
public interface OrderWebService {

    @RequestMapping("/checkTradeCode")
    String checkTradeCode(@RequestParam("memberId") Long memberId, @RequestParam("tradeCode") String tradeCode);

    @RequestMapping("/generateTradeCode")
    String generateTradeCode(@RequestParam("memberId") Long memberId);


    @RequestMapping("/saveOrder")
    void saveOrder(@RequestBody OmsOrder omsOrder);
}

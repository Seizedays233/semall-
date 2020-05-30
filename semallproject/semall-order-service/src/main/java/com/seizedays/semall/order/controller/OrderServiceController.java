package com.seizedays.semall.order.controller;

import com.seizedays.semall.beans.OmsOrder;
import com.seizedays.semall.services.OrderService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.math.BigDecimal;

@RestController
@CrossOrigin
public class OrderServiceController {

    @Resource
    OrderService orderService;

    @RequestMapping("/checkTradeCode")
    public String checkTradeCode(@RequestParam("memberId") Long memberId, @RequestParam("tradeCode") String tradeCode) {
        return orderService.checkTradeCode(memberId, tradeCode);
    }

    @RequestMapping("/generateTradeCode")
    public String generateTradeCode(@RequestParam("memberId") Long memberId) {
        return orderService.generateTradeCode(memberId);
    }

    @RequestMapping("/saveOrder")
    void saveOrder(@RequestBody OmsOrder omsOrder){
        orderService.saveOrder(omsOrder);
    }


}

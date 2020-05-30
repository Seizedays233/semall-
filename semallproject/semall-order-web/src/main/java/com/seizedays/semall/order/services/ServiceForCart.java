package com.seizedays.semall.order.services;

import com.seizedays.semall.beans.OmsCartItem;
import com.seizedays.semall.webutils.configs.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "semall-cart-service",configuration = FeignConfig.class)
public interface ServiceForCart {

    @RequestMapping("/getCartList")
    List<OmsCartItem> cartList(@RequestParam("memberId") Long memberId);
}

package com.seizedays.semall.cart.services;

import com.seizedays.semall.beans.OmsCartItem;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.seizedays.semall.webutils.configs.FeignConfig;

import java.util.List;

@FeignClient(value = "semall-cart-service",configuration = FeignConfig.class)
public interface CartWebService {

    @RequestMapping("/getCarByUser")
    OmsCartItem getCartsByUser(@RequestParam("memberId") Long memberId, @RequestParam("skuId") Long skuId);

    @RequestMapping("/addCart")
    void addCart(@RequestBody OmsCartItem omsCartItem);

    @RequestMapping("/updateCart")
    void updateCart(@RequestBody OmsCartItem omsCartItemFromDb);

    @RequestMapping("/flushCartCache")
    void flushCartCache(@RequestParam("memberId") Long memberId);

    @RequestMapping("/getCartList")
    List<OmsCartItem> cartList(@RequestParam("memberId") Long memberId);

    @RequestMapping("/updateCheckCart")
    void checkCart(@RequestBody OmsCartItem omsCartItem);
}

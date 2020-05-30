package com.seizedays.semall.cart.controllers;

import com.seizedays.semall.beans.OmsCartItem;
import com.seizedays.semall.services.CartService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.entity.Example;

import javax.annotation.Resource;
import java.util.List;

@RestController
@CrossOrigin
public class CartServiceController {

    @Resource
    CartService cartService;

    @RequestMapping("/getCarByUser")
    public OmsCartItem getCartsByUser(@RequestParam("memberId") Long memberId, @RequestParam("skuId") Long skuId) {
        return cartService.getCartsByUser(memberId, skuId);
    }

    @RequestMapping("/addCart")
    public void addCart(@RequestBody OmsCartItem omsCartItem) {
        cartService.addCart(omsCartItem);

    }

    @RequestMapping("/updateCart")
    public void updateCart(@RequestBody OmsCartItem omsCartItemFromDb) {
        cartService.updateCart(omsCartItemFromDb);
    }

    @RequestMapping("/flushCartCache")
    public void flushCartCache(@RequestParam("memberId") Long memberId) {
        cartService.flushCartCache(memberId);
    }

    @RequestMapping("/getCartList")
    public List<OmsCartItem> cartList(@RequestParam("memberId") Long memberId){
        return cartService.cartList(memberId);
    }

    @RequestMapping("/updateCheckCart")
    void checkCart(@RequestBody OmsCartItem omsCartItem){

        cartService.checkCart(omsCartItem);
    }
}

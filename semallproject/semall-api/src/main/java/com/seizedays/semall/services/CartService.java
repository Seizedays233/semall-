package com.seizedays.semall.services;

import com.seizedays.semall.beans.OmsCartItem;

import java.util.List;

public interface CartService {
    OmsCartItem getCartsByUser(Long memberId, Long skuId);

    void addCart(OmsCartItem omsCartItem);

    void updateCart(OmsCartItem omsCartItemFromDb);

    void flushCartCache(Long memberId);

    List<OmsCartItem> cartList(Long memberId);

    void checkCart(OmsCartItem omsCartItem);
}

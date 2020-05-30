package com.seizedays.semall.services;

import com.seizedays.semall.beans.OmsOrder;

import java.math.BigDecimal;

public interface OrderService {
    String checkTradeCode(Long memberId, String tradeCode);

    String generateTradeCode(Long memberId);

    void saveOrder(OmsOrder omsOrder);
}

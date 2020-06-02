package com.seizedays.semall.order.mq;

import com.seizedays.semall.beans.OmsOrder;
import com.seizedays.semall.services.OrderService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.MapMessage;

@Component
public class OrderServiceMQListener {

    @Autowired
    OrderService orderService;

    @JmsListener(destination = "PAYMENT_SUCCESS_QUEUE", containerFactory = "jmsQueueListener")
    public void consumePaymentResult(MapMessage mapMessage){

        try {
            String outTradeNo = mapMessage.getString("out_trade_no");
            if(StringUtils.isNotBlank(outTradeNo)){
                OmsOrder omsOrder = new OmsOrder();
                omsOrder.setOrderSn(outTradeNo);
                orderService.updateOrder(omsOrder);
            }
        } catch (JMSException e) {
            e.printStackTrace();
        }

        //更新订单状态业务

    }
}

package com.seizedays.semall.payment.mq;

import com.seizedays.semall.services.PaymentService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import java.util.Map;

@Component
public class PaymentServiceMQListener {

    @Autowired
    PaymentService paymentService;

    @JmsListener(destination = "PAYMENT_CHECK_QUEUE", containerFactory = "jmsQueueListener")
    public void consumePaymentCheckResult(MapMessage mapMessage) throws JMSException {
        String outTradeNo = mapMessage.getString("out_trade_no");
        Integer count = mapMessage.getInt("count");

        //调用paymentService支付宝检查接口
        Map<String, Object> resultMap = paymentService.checkAlipayPayment(outTradeNo);

        if (null == resultMap || resultMap.isEmpty()){
            if (count > 0) {
                System.out.println("未支付成功，重新发送延迟检查");
                count--;
                paymentService.delayPaymentResultCheck(outTradeNo, count);
            }else {
                System.out.println("检查次数用尽，结束检查");
            }
        }else {
            String tradeStatus = (String) resultMap.get("trade_status");

            //根据查询的支付状态结果 判断是否进行下一次的队列延迟任务
            //支付失败 延迟 支付成功 继续后续的数据更新及其他任务
            if (StringUtils.isNotBlank(tradeStatus) && tradeStatus.equals("TRADE_SUCCESS")) {
                //更新支付 发送支付队列
                //paymentService.updatPayment(null);
            } else {
                //继续发送延迟检查任务 计算延迟时间
                if (count > 0) {
                    System.out.println("未支付成功，重新发送延迟检查");
                    count--;
                    paymentService.delayPaymentResultCheck(outTradeNo, count);
                }else {
                    System.out.println("检查次数用尽，结束检查");
                }
            }
        }

    }
}

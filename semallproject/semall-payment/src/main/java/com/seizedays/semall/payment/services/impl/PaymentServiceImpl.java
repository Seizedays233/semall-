package com.seizedays.semall.payment.services.impl;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.alipay.api.response.AlipayTradeQueryResponse;
import com.seizedays.semall.beans.PaymentInfo;
import com.seizedays.semall.mq.ActiveMQUtil;
import com.seizedays.semall.payment.mappers.PaymentInfoMapper;
import com.seizedays.semall.services.PaymentService;
import org.apache.activemq.ScheduledMessage;
import org.apache.activemq.command.ActiveMQMapMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import javax.jms.*;
import java.util.HashMap;
import java.util.Map;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    PaymentInfoMapper paymentInfoMapper;

    @Autowired
    ActiveMQUtil activeMQUtil;

    @Autowired
    AlipayClient alipayClient;

    @Override
    public void savePaymentInfo(PaymentInfo paymentInfo) {
        paymentInfoMapper.insertSelective(paymentInfo);
    }

    @Override
    public void updatPayment(PaymentInfo paymentInfo) {
        Example e = new Example(PaymentInfo.class);
        e.createCriteria().andEqualTo("id", paymentInfo.getId());

        Connection connection = null;
        Session session = null;
        try {
            connection = activeMQUtil.getConnectionFactory().createConnection();
            session = connection.createSession(true, Session.SESSION_TRANSACTED);
        } catch (JMSException ex) {
            ex.printStackTrace();
        }

        if (session != null) {
            try {
                paymentInfoMapper.updateByExample(paymentInfo, e);
                //支付成功后 引起的系统服务 -> 订单服务更新 ->库存服务 -> 物流服务
                // 调用Mq 发起支付成功消息
                Queue paymentSuccessQueue = session.createQueue("PAYMENT_SUCCESS_QUEUE");
                MessageProducer producer = session.createProducer(paymentSuccessQueue);

                MapMessage mapMessage = new ActiveMQMapMessage();
                mapMessage.setString("out_trade_no", paymentInfo.getOrderSn());

                producer.send(mapMessage);
                session.commit();
            } catch (JMSException e2) {
                //回滚
                try {
                    session.rollback();
                } catch (JMSException ex) {
                    ex.printStackTrace();
                }
            } finally {
                try {
                    connection.close();
                } catch (JMSException ex) {
                    ex.printStackTrace();
                }
            }
        }


    }

    @Override
    public void delayPaymentResultCheck(String outTradeNo, int count) {
        Connection connection = null;
        Session session = null;
        try {
            connection = activeMQUtil.getConnectionFactory().createConnection();
            session = connection.createSession(true, Session.SESSION_TRANSACTED);
        } catch (JMSException ex) {
            ex.printStackTrace();
        }

        if (session != null) {
            try {
                Queue paymentSuccessQueue = session.createQueue("PAYMENT_CHECK_QUEUE");
                MessageProducer producer = session.createProducer(paymentSuccessQueue);

                MapMessage mapMessage = new ActiveMQMapMessage();
                mapMessage.setString("out_trade_no", outTradeNo);
                mapMessage.setInt("count", count);
                //设置消息延迟时间
                mapMessage.setLongProperty(ScheduledMessage.AMQ_SCHEDULED_DELAY, 1000*30);
                producer.send(mapMessage);
                session.commit();
            } catch (JMSException e2) {
                //回滚
                try {
                    session.rollback();
                } catch (JMSException ex) {
                    ex.printStackTrace();
                }
            } finally {
                try {
                    connection.close();
                } catch (JMSException ex) {
                    ex.printStackTrace();
                }
            }
        }
    }

    @Override
    public Map<String, Object> checkAlipayPayment(String outTradeNo) {

        Map<String,Object> resultMap = new HashMap<>();

        AlipayTradeQueryRequest request = new AlipayTradeQueryRequest();
        Map<String,Object> requestMap = new HashMap<>();
        requestMap.put("out_trade_no",outTradeNo);
        request.setBizContent(JSON.toJSONString(requestMap));
        AlipayTradeQueryResponse response = null;
        try {
            response = alipayClient.execute(request);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        if(response.isSuccess()){
            System.out.println("有可能交易已创建，调用成功");
            resultMap.put("out_trade_no",response.getOutTradeNo());
            resultMap.put("trade_no",response.getTradeNo());
            resultMap.put("trade_status",response.getTradeStatus());
            resultMap.put("call_back_content",response.getMsg());
        } else {
            System.out.println("有可能交易未创建，调用失败");

        }

        return resultMap;
    }
}

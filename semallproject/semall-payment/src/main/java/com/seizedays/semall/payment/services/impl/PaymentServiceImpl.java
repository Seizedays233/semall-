package com.seizedays.semall.payment.services.impl;

import com.seizedays.semall.beans.PaymentInfo;
import com.seizedays.semall.payment.mappers.PaymentInfoMapper;
import com.seizedays.semall.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    PaymentInfoMapper paymentInfoMapper;

    @Override
    public void savePaymentInfo(PaymentInfo paymentInfo) {
        paymentInfoMapper.insertSelective(paymentInfo);
    }

    @Override
    public void updatPayment(PaymentInfo paymentInfo) {
        Example e = new Example(PaymentInfo.class);
        e.createCriteria().andEqualTo("id", paymentInfo.getId());
        paymentInfoMapper.updateByExample(paymentInfo,e);
    }
}

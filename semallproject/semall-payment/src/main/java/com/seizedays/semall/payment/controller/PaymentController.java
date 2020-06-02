package com.seizedays.semall.payment.controller;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.seizedays.semall.annotations.LoginRequired;
import com.seizedays.semall.beans.OmsOrder;
import com.seizedays.semall.beans.PaymentInfo;
import com.seizedays.semall.payment.config.AlipayConfig;
import com.seizedays.semall.payment.services.OrderServiceForPayment;
import com.seizedays.semall.services.PaymentService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
@CrossOrigin
public class PaymentController {

    @Autowired
    AlipayClient alipayClient;

    @Resource
    PaymentService paymentService;

    @Resource
    OrderServiceForPayment orderService;

    @RequestMapping("/alipay/callback/return")
    @LoginRequired(loginSuccessRequired = true)
    public String alipayCallbackReturn(HttpServletRequest request, ModelMap modelMap) {

        //从回调请求中获取支付宝的参数 验签
        String sign = request.getParameter("sign");
        String aliPayTradeNo = request.getParameter("trade_no");
        String outTradeNo = request.getParameter("out_trade_no");
        String totalAmount = request.getParameter("total_amount");
        String trade_status = request.getParameter("trade_status");
        String subject = request.getParameter("subject");

        String callBackContent = request.getQueryString();
        //以前是通过支付宝的paramMap进行签名验证， 2.0版本之后的接口将这个参数去掉了， 导致同步请求没法验签
        //这里简单验证一下
        if (StringUtils.isNotBlank(sign)) {
            //验签成功
            //更新用户的支付信息
            PaymentInfo paymentInfo = new PaymentInfo();
            paymentInfo.setOrderSn(outTradeNo);
            paymentInfo.setPaymentStatus("已经支付");
            paymentInfo.setAlipayTradeNo(aliPayTradeNo);  //支付宝的交易凭证号
            paymentInfo.setCallbackContent(callBackContent); //回调请求字符串
            paymentInfo.setCallbackTime(new Date());

            paymentService.updatPayment(paymentInfo);

        }



        return "finish";
    }

    @RequestMapping("/mx/submit")
    @LoginRequired(loginSuccessRequired = true)
    public String mx(@RequestParam("outTradeNo") String outTradeNo,
                     @RequestParam("totalAmount") BigDecimal totalAmount,
                     HttpServletRequest request,
                     ModelMap modelMap) {
        return null;
    }

    @RequestMapping("/alipay/submit")
    @LoginRequired(loginSuccessRequired = true)
    @ResponseBody
    public String alipay(@RequestParam("outTradeNo") String outTradeNo,
                         @RequestParam("totalAmount") BigDecimal totalAmount,
                         HttpServletRequest request,
                         ModelMap modelMap) {

        String form = "";
        //获得一个支付宝请求的客户端 它不是一个链接 而是一个封装好的http表单请求
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest(); //创建api对应的request
        Map<String, Object> map = new HashMap<>();

        map.put("out_trade_no", outTradeNo);
        map.put("product_code", "FAST_INSTANT_TRADE_PAY");
        map.put("total_amount", totalAmount);
        map.put("subject", "semall商品结算");

        String param = JSON.toJSONString(map);
        alipayRequest.setBizContent(param);

        //回调函数地址
        alipayRequest.setNotifyUrl(AlipayConfig.notify_payment_url);
        alipayRequest.setReturnUrl(AlipayConfig.return_payment_url);

        try {
            form = alipayClient.pageExecute(alipayRequest).getBody();//调用sdk生成表单
            System.out.println(form);
        } catch (AlipayApiException e) {
            e.printStackTrace();
        }
        //生成并保存用户支付信息
        OmsOrder omsOrder = orderService.getOrderByOutTradeNo(outTradeNo);
        PaymentInfo paymentInfo = new PaymentInfo();
        paymentInfo.setCreateTime(new Date());
        paymentInfo.setOrderSn(outTradeNo);
        paymentInfo.setOrderId(omsOrder.getId());
        paymentInfo.setTotalAmount(omsOrder.getTotalAmount());
        paymentInfo.setPaymentStatus("订单未付款");
        paymentInfo.setSubject("semall商品");

        paymentService.savePaymentInfo(paymentInfo);

        //向mq发送一个检查支付状态的延迟消息队列
        paymentService.delayPaymentResultCheck(outTradeNo,5);

        return form;
    }

    @RequestMapping("/index")
    @LoginRequired(loginSuccessRequired = true)
    public String redirect(@RequestParam("outTradeNo") String outTradeNo,
                           @RequestParam("totalAmount") BigDecimal totalAmount,
                           HttpServletRequest request,
                           ModelMap modelMap
    ) {

        String nickName = (String) request.getAttribute("nickname");
        modelMap.put("nickName", nickName);
        modelMap.put("outTradeNo", outTradeNo);
        modelMap.put("totalAmount", totalAmount);
        return "index";
    }
}

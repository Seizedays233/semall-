package com.seizedays.semall.payment.controller;

import com.alibaba.fastjson.JSON;
import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.seizedays.semall.annotations.LoginRequired;
import com.seizedays.semall.payment.config.AlipayConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Controller
@CrossOrigin
public class PaymentController {

    @Autowired
    AlipayClient alipayClient;

    @RequestMapping("/alipay/callback/return")
    @LoginRequired(loginSuccessRequired = true)
    public String alipayCallbackReturn(HttpServletRequest request, ModelMap modelMap){



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

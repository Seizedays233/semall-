package com.seizedays.semall.order.controllers;

import com.seizedays.semall.annotations.LoginRequired;
import com.seizedays.semall.beans.OmsCartItem;
import com.seizedays.semall.beans.OmsOrder;
import com.seizedays.semall.beans.OmsOrderItem;
import com.seizedays.semall.beans.UmsMemberReceiveAddress;
import com.seizedays.semall.order.services.OrderWebService;
import com.seizedays.semall.order.services.ServiceForCart;
import com.seizedays.semall.order.services.ServiceForSku;
import com.seizedays.semall.order.services.ServiceForUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Controller
@CrossOrigin
public class OrderWebController {

    @Resource
    ServiceForCart cartService;

    @Resource
    ServiceForUser userService;

    @Resource
    OrderWebService orderWebService;

    @Resource
    ServiceForSku skuService;

    @RequestMapping("/toTrade")
    @LoginRequired(loginSuccessRequired = true)
    public String toTrade(HttpServletRequest request, HttpServletResponse response, ModelMap modelMap) {


        Long memberId = Long.valueOf((String) request.getAttribute("memberId"));
        String nickName = (String) request.getAttribute("nickName");

        //用户收件人地址列表
        List<UmsMemberReceiveAddress> receiveAddresses = userService.getReceiveAddressByMemberId(memberId);

        //将购物车集合转换为结算清单集合
        List<OmsCartItem> omsCartItems = cartService.cartList(memberId);

        List<OmsOrderItem> omsOrderItems = new ArrayList<>();

        for (OmsCartItem omsCartItem : omsCartItems) {
            //每次循环一个购物车对象， 就封装一个商品的详情到OrderItems里面
            if (omsCartItem.getIsChecked().equals("1")) {
                OmsOrderItem omsOrderItem = new OmsOrderItem();
                omsOrderItem.setProductId(omsCartItem.getProductId());
                omsOrderItem.setProductName(omsCartItem.getProductName());
                omsOrderItem.setProductPic(omsCartItem.getProductPic());
                omsOrderItem.setProductPrice(omsCartItem.getPrice());
                omsOrderItem.setProductQuantity(omsCartItem.getQuantity());

                omsOrderItems.add(omsOrderItem);
            }
        }

        modelMap.put("omsOrderItems", omsOrderItems);
        modelMap.put("userAddressList", receiveAddresses);
        modelMap.put("totalAmount", getTotalAmount(omsCartItems));

        //生成交易码 用于订单在提交时的校验
        String tradeCode = orderWebService.generateTradeCode(memberId);
        modelMap.put("tradeCode", tradeCode);
        return "trade";
    }

    private BigDecimal getTotalAmount(List<OmsCartItem> omsCartItems) {
        BigDecimal totalAmount = new BigDecimal("0");
        for (OmsCartItem omsCartItem : omsCartItems) {
            BigDecimal totalPrice = omsCartItem.getTotalPrice();
            if (omsCartItem.getIsChecked().equals("1")){
                totalAmount = totalAmount.add(totalPrice);
            }
        }

        return totalAmount;
    }

    @RequestMapping("/submitOrder")
    @LoginRequired(loginSuccessRequired = true)
    public ModelAndView submitOrder(String receiveAddressId, BigDecimal totalAmount, String tradeCode,ModelMap modelMap, HttpServletRequest request, HttpServletResponse response, HttpSession session){
        Long memberId = Long.valueOf((String) request.getAttribute("memberId"));
        String nickName = (String) request.getAttribute("nickName");
        ModelAndView mv = new ModelAndView("redirect:http://localhost:8087");
        //检查交易码
        String success = orderWebService.checkTradeCode(memberId,tradeCode);

        if (success.equals("success")){

            List<OmsOrderItem> omsOrderItems = new ArrayList<>();
            //订单对象
            SimpleDateFormat sdf = new SimpleDateFormat("YYYYMMDDHHmmss");
            String outTradeNo = "semall";
            String time = sdf.format(new Date());
            outTradeNo += time; //将毫秒时间戳拼接到外部订单号
            OmsOrder omsOrder = new OmsOrder();
            omsOrder.setCreateTime(new Date());
            omsOrder.setAutoConfirmDay(7);
            omsOrder.setMemberId(memberId);
            omsOrder.setMemberUsername(nickName);
            omsOrder.setNote("订单备注");
            omsOrder.setOrderSn(outTradeNo);
            omsOrder.setPayAmount(totalAmount);
            omsOrder.setOrderType(0);
            UmsMemberReceiveAddress umsMemberReceiveAddress = userService.getReceiveAddressByAddressId(receiveAddressId);
            omsOrder.setReceiverCity(umsMemberReceiveAddress.getCity());
            omsOrder.setReceiverDetailAddress(umsMemberReceiveAddress.getDetailAddress());
            omsOrder.setReceiverName(umsMemberReceiveAddress.getName());
            omsOrder.setReceiverPhone(umsMemberReceiveAddress.getPhoneNumber());
            omsOrder.setReceiverPostCode(umsMemberReceiveAddress.getPostCode());
            omsOrder.setReceiverProvince(umsMemberReceiveAddress.getProvince());
            omsOrder.setReceiverRegion(umsMemberReceiveAddress.getRegion());

            //当前日期加一天
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE,1);
            Date time1 = calendar.getTime();
            omsOrder.setReceiveTime(time1);

            omsOrder.setSourceType(0);
            omsOrder.setStatus(0);
            omsOrder.setTotalAmount(totalAmount);

            //根据用户id获得要购买的商品列表集合（从购物车中取）
            List<OmsCartItem> omsCartItems = cartService.cartList(memberId);
            for (OmsCartItem omsCartItem : omsCartItems) {
                if (omsCartItem.getIsChecked().equals("1")){
                    //获得订单详情列表
                    OmsOrderItem omsOrderItem = new OmsOrderItem();
                    boolean check = skuService.checkPrice(omsCartItem.getProductSkuId(), omsCartItem.getPrice());
                    if (!check){
                        return new ModelAndView("tradeFail");
                    }

                    omsOrderItem.setProductPic(omsCartItem.getProductPic());
                    omsOrderItem.setProductName(omsCartItem.getProductName());

                    omsOrderItem.setOrderSn(outTradeNo);  //外部订单号 用来和其他系统进行交互 防止重复
                    omsOrderItem.setProductCategoryId(omsCartItem.getProductCategoryId());
                    omsOrderItem.setProductPrice(omsCartItem.getPrice());
                    omsOrderItem.setProductQuantity(omsCartItem.getQuantity());
                    omsOrderItem.setRealAmount(omsCartItem.getTotalPrice());
                    omsOrderItem.setProductSkuId(omsCartItem.getProductSkuId());
                    omsOrderItem.setProductSn("仓库对应的商品编号");
                    omsOrderItem.setProductSkuCode("123456");
                    omsOrderItem.setProductId(omsCartItem.getProductId());
                    omsOrderItem.setProductBrand(omsCartItem.getProductBrand());
                    omsOrderItem.setProductName(omsCartItem.getProductName());
                    omsOrderItem.setProductAttr(omsCartItem.getProductAttr());


                    omsOrderItems.add(omsOrderItem);
                }
            }
            //检验库存 调用库存系统

            omsOrder.setOmsOrderItems(omsOrderItems);

            //将订单和订单详情写入数据库
            //删除购物车中对应的商品
            orderWebService.saveOrder(omsOrder);

            //重定向到支付系统
            mv = new ModelAndView("redirect:http://localhost:8087/index");
            mv.addObject("outTradeNo", outTradeNo);
            mv.addObject("totalAmount", totalAmount);

        }else {
            mv = new ModelAndView("tradeFail");
        }

        return mv;
    }
}

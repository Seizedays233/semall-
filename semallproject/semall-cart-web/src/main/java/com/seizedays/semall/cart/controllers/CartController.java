package com.seizedays.semall.cart.controllers;

import com.alibaba.fastjson.JSON;
import com.seizedays.semall.annotations.LoginRequired;
import com.seizedays.semall.beans.OmsCartItem;
import com.seizedays.semall.beans.PmsSkuInfo;
import com.seizedays.semall.cart.services.CartWebService;
import com.seizedays.semall.cart.services.SkuCartWebService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import com.seizedays.semall.webutils.CookieUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.*;

@Controller
@CrossOrigin
public class CartController {

    @Resource
    SkuCartWebService skuCartWebService;

    @Resource
    CartWebService cartWebService;

    @RequestMapping("/addToCart")
    public String addToCart(Long skuId, Integer quantity, HttpServletRequest request, HttpServletResponse response) {

        List<OmsCartItem> omsCartItems = new ArrayList<>();

        //1. 调用商品服务 查询商品信息
        PmsSkuInfo pmsSkuInfo = skuCartWebService.getSkuById(skuId);

        //2. 将商品信息封装成购购物车信息
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setCreateDate(new Date());
        omsCartItem.setDeleteStatus(0);
        omsCartItem.setPrice(pmsSkuInfo.getPrice());
        omsCartItem.setProductAttr("");
        omsCartItem.setProductCategoryId(pmsSkuInfo.getCatalog3Id());
        omsCartItem.setProductId(pmsSkuInfo.getSpuId());
        omsCartItem.setProductName(pmsSkuInfo.getSkuName());
        omsCartItem.setProductPic(pmsSkuInfo.getSkuDefaultImg());
        omsCartItem.setProductSkuId(pmsSkuInfo.getId());
        omsCartItem.setQuantity(quantity);
        omsCartItem.setIsChecked("1");

        //3. 判断用户是否登录
        Long memberId = 1L;
        //request.getAttribute("memberId");
        //4. 根据用户登录状态决定将购物车信息存入cookie还是data base
        if (0L == memberId) {
            //用户未登录 存入cookie
            //检查并更新cookie


            //cookie中原有的购物车数据
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);

            if (StringUtils.isBlank(cartListCookie)) {
                //cookie为空
                omsCartItems.add(omsCartItem);
            } else {
                //cookie不为空
                omsCartItems = JSON.parseArray(cartListCookie, OmsCartItem.class);
                //判断添加的数据再cookie中是否存在
                boolean exist = ifCartExist(omsCartItem, omsCartItems);
                if (exist) {
                    //之前添加过 更新购物车的添加数量
                    for (OmsCartItem cartItem : omsCartItems) {
                        if (cartItem.getProductSkuId().equals(omsCartItem.getProductSkuId())) {
                            cartItem.setQuantity(cartItem.getQuantity() + quantity);
                            cartItem.setPrice(cartItem.getPrice().add(omsCartItem.getPrice()));
                        }
                    }

                } else {
                    //之前没有添加过
                    omsCartItems.add(omsCartItem);
                }

                CookieUtil.setCookie(request, response, "cartListCookie", JSON.toJSONString(omsCartItems), 60 * 60 * 72, true);
            }

        } else {
            //用户已登录 存入 data base
            OmsCartItem omsCartItemFromDb = cartWebService.getCartsByUser(memberId, skuId);

            if (omsCartItemFromDb == null) {
                //该用户没有添加过当前商品
                omsCartItem.setMemberId(memberId);
                cartWebService.addCart(omsCartItem);

            } else {
                //该用户添加过当前商品
                omsCartItemFromDb.setQuantity(omsCartItemFromDb.getQuantity() + quantity);
                omsCartItemFromDb.setPrice(omsCartItemFromDb.getPrice().add(omsCartItem.getPrice()));
                cartWebService.updateCart(omsCartItemFromDb);
            }


            //同步缓存
            cartWebService.flushCartCache(memberId);

        }

        return "redirect:/success.html";
    }

    private boolean ifCartExist(OmsCartItem omsCartItem, List<OmsCartItem> omsCartItems) {

        boolean exist = false;

        for (OmsCartItem cartItem : omsCartItems) {
            Long productSkuId = cartItem.getProductSkuId();
            if (productSkuId.equals(omsCartItem.getProductSkuId())) {
                exist = true;
                break;
            }
        }

        return exist;

    }

    @RequestMapping("/cartList")
    public String cartList(HttpServletRequest request,  ModelMap modelMap) {

        List<OmsCartItem> omsCartItemList = new ArrayList<>();

        Long memberId = 1L;


        if (memberId != null) {
            //已经登录 查询db
            omsCartItemList = cartWebService.cartList(memberId);
        } else {
            // 未登录 查询cookie
            String cartListCookie = CookieUtil.getCookieValue(request, "cartListCookie", true);
            if (StringUtils.isNotBlank(cartListCookie)) {
                omsCartItemList = JSON.parseArray(cartListCookie, OmsCartItem.class);
            }
        }

        for (OmsCartItem omsCartItem : omsCartItemList) {
            omsCartItem.setTotalPrice(omsCartItem.getPrice().multiply(new BigDecimal(omsCartItem.getQuantity())));
        }

        Collections.sort(omsCartItemList, (o1, o2) -> {
            int num = o1.getId().intValue() - o2.getId().intValue();
            int num2 = num == 0 ? o1.getId() .compareTo(o2.getId() ):num;
            return num2;
        });

        modelMap.put("cartList", omsCartItemList);
        //被勾选的商品的结算总价
        BigDecimal totalValue = getTotalValue(omsCartItemList);
        modelMap.put("totalValue", totalValue);
        return "cartList";
    }

    private BigDecimal getTotalValue(List<OmsCartItem> omsCartItemList) {
        BigDecimal totalValue = new BigDecimal(0.00);
        for (OmsCartItem omsCartItem : omsCartItemList) {
            if (omsCartItem.getIsChecked().equals("1")){
                totalValue = totalValue.add(omsCartItem.getTotalPrice());
            }
        }

        return totalValue;
    }

    @RequestMapping("/checkCart")
    @LoginRequired(loginSuccessRequired = false)
    public String checkCart(Long skuId, String isChecked, ModelMap modelMap){
        Long memberId = 1L;
        //调用服务 修改checked状态
        OmsCartItem omsCartItem = new OmsCartItem();
        omsCartItem.setMemberId(memberId);
        omsCartItem.setProductSkuId(skuId);
        omsCartItem.setIsChecked(isChecked);

        cartWebService.checkCart(omsCartItem);

        //将最新数据从缓存中查出 渲染内嵌页面
        List<OmsCartItem> omsCartItems = cartWebService.cartList(memberId);
        Collections.sort(omsCartItems, (o1, o2) -> {
            int num = o1.getId().intValue() - o2.getId().intValue();
            int num2 = num == 0 ? o1.getId() .compareTo(o2.getId() ):num;
            return num2;
        });

        for (OmsCartItem omsCartItem1 : omsCartItems) {
            System.out.println(omsCartItem1.getId());
        }
        modelMap.put("cartList", omsCartItems);

        //被勾选的商品的结算总价
        BigDecimal totalValue = getTotalValue(omsCartItems);
        modelMap.put("totalValue", totalValue);

        return "cartListInner";
    }



}

package com.seizedays.semall.manage.controller;

import com.seizedays.semall.beans.PmsSkuInfo;
import com.seizedays.semall.services.SkuService;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tk.mybatis.mapper.util.StringUtil;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.List;

@Controller
@CrossOrigin
public class SkuController {

    @Resource
    SkuService skuService;

    @RequestMapping(value = "/service/saveSkuInfo",consumes= MediaType.APPLICATION_JSON_UTF8_VALUE,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String saveSkuInfo(@RequestBody PmsSkuInfo pmsSkuInfo) {

        //处理默认图片
        String skuDefaultImg = pmsSkuInfo.getSkuDefaultImg();
        if (StringUtils.isBlank(skuDefaultImg)){
            pmsSkuInfo.setSkuDefaultImg(pmsSkuInfo.getSkuImageList().get(0).getImgUrl());
        }

        String message = skuService.saveSkuInfo(pmsSkuInfo);

        return message;
    }

    @RequestMapping("/service/getSkuSaleAttrValue")
    @ResponseBody
    public List<PmsSkuInfo> getSkuSaleAttrValueListBySpu(@RequestParam("spuId") Long spuId){
        return skuService.getSkuSaleAttrValueListBySpu(spuId);
    }


    @RequestMapping("/service/getSku")
    @ResponseBody
    public PmsSkuInfo getSkuById(@RequestParam("skuId") Long skuId){
        PmsSkuInfo pmsSkuInfo = skuService.getSkuById(skuId);
        return pmsSkuInfo;
    }


    @RequestMapping("/checkPrice")
    @ResponseBody
    public boolean checkPrice(@RequestParam("productSkuId") Long productSkuId,@RequestParam("productPrice") BigDecimal productPrice){
        return skuService.checkPrice(productSkuId, productPrice);
    }


}

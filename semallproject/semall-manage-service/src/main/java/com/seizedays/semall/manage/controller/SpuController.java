package com.seizedays.semall.manage.controller;

import com.seizedays.semall.beans.PmsProductImage;
import com.seizedays.semall.beans.PmsProductInfo;
import com.seizedays.semall.beans.PmsProductSaleAttr;
import com.seizedays.semall.beans.PmsSkuInfo;
import com.seizedays.semall.services.SpuService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@CrossOrigin
public class SpuController {

    @Resource
    SpuService spuService;

    @RequestMapping("/service/spuList")
    @ResponseBody
    public List<PmsProductInfo> spuList(@RequestParam("catalog3Id") Long catalog3Id) {
        return spuService.spuList(catalog3Id);
    }

    @RequestMapping(value = "/service/saveSpuInfo",consumes= MediaType.APPLICATION_JSON_UTF8_VALUE,produces=MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String saveSpuInfo(@RequestBody PmsProductInfo pmsProductInfo) {
        String message = spuService.saveSpuInfo(pmsProductInfo);
        return message;
    }

    @RequestMapping("/service/spuSaleAttrList")
    @ResponseBody
    public List<PmsProductSaleAttr> spuSaleAttrList(@RequestParam("spuId") Long spuId, @RequestParam(value = "skuId", required = false) Long skuId){
        if (null != skuId){
            return spuService.spuSaleAttrList(spuId, skuId);
        }

        return spuService.spuSaleAttrList(spuId);
    }

    @RequestMapping("/service/spuImageList")
    @ResponseBody
    public List<PmsProductImage> spuImageList(@RequestParam("spuId") Long spuId){
        List<PmsProductImage> spuImages = spuService.spuImageList(spuId);
        System.out.println(spuImages);
        return spuImages;
    }


}

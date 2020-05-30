package com.seizedays.semall.manage.controllers;

import com.seizedays.semall.beans.PmsProductImage;
import com.seizedays.semall.beans.PmsProductInfo;
import com.seizedays.semall.beans.PmsProductSaleAttr;
import com.seizedays.semall.manage.services.SpuWebService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@CrossOrigin
public class SpuWebController {

    @Resource
    SpuWebService spuWebService;

    @RequestMapping("/spuList")
    @ResponseBody
    public List<PmsProductInfo> spuList(@RequestParam("catalog3Id") Long catalog3Id){
        return spuWebService.spuList(catalog3Id);
    }

    @RequestMapping(value = "/saveSpuInfo",consumes= MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String saveSubInfo(@RequestBody PmsProductInfo pmsProductInfo){

        String message = spuWebService.saveSpuInfo(pmsProductInfo);

        return message;
    }

    @RequestMapping("/spuSaleAttrList")
    @ResponseBody
    public List<PmsProductSaleAttr> spuSaleAttrList(@RequestParam("spuId") Long spuId){
        return spuWebService.spuSaleAttrList(spuId);
    }

    @RequestMapping("/spuImageList")
    @ResponseBody
    public List<PmsProductImage> spuImageList(@RequestParam("spuId") Long spuId){
        return spuWebService.spuImageList(spuId);
    }

}

package com.seizedays.semall.manage.services;

import com.seizedays.semall.beans.PmsProductImage;
import com.seizedays.semall.beans.PmsProductInfo;
import com.seizedays.semall.beans.PmsProductSaleAttr;
import com.seizedays.semall.webutils.configs.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@FeignClient(value = "semall-manage-service",configuration = FeignConfig.class)
public interface SpuWebService {

    @RequestMapping("/service/spuList")
    List<PmsProductInfo> spuList(@RequestParam("catalog3Id") Long catalog3Id);

    @RequestMapping("/service/saveSpuInfo")
    String saveSpuInfo(@RequestBody PmsProductInfo pmsProductInfo);

    @RequestMapping("/service/spuSaleAttrList")
    List<PmsProductSaleAttr> spuSaleAttrList(@RequestParam("spuId") Long spuId);

    @RequestMapping("/service/spuImageList")
    List<PmsProductImage> spuImageList(@RequestParam("spuId") Long spuId);

}

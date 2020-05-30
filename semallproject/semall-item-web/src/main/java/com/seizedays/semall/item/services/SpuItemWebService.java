package com.seizedays.semall.item.services;

import com.seizedays.semall.beans.PmsProductSaleAttr;
import com.seizedays.semall.item.configs.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "semall-manage-service",configuration = FeignConfig.class)
public interface SpuItemWebService {

    @RequestMapping("/service/spuSaleAttrList")
    List<PmsProductSaleAttr> spuSaleAttrListCheckBySku(@RequestParam("spuId") Long spuId, @RequestParam("skuId") Long skuId);
}

package com.seizedays.semall.item.services;

import com.seizedays.semall.beans.PmsSkuInfo;
import com.seizedays.semall.item.configs.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "semall-manage-service",configuration = FeignConfig.class)
public interface SkuItemWebService {

    @RequestMapping("/service/getSku")
    PmsSkuInfo getSkuById(@RequestParam(value = "skuId") Long skuId);

    @RequestMapping("/service/getSkuSaleAttrValue")
    List<PmsSkuInfo> getSkuSaleAttrValueListBySpu(@RequestParam("spuId") Long spuId);
}

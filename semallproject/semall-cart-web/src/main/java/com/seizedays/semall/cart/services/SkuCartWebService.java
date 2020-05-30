package com.seizedays.semall.cart.services;

import com.seizedays.semall.beans.PmsSkuInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.seizedays.semall.webutils.configs.FeignConfig;

@FeignClient(value = "semall-manage-service",configuration = FeignConfig.class)
public interface SkuCartWebService {

    @RequestMapping("/service/getSku")
    PmsSkuInfo getSkuById(@RequestParam(value = "skuId") Long skuId);
}

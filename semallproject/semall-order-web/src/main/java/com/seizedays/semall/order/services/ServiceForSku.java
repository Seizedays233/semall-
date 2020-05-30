package com.seizedays.semall.order.services;

import com.seizedays.semall.webutils.configs.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@FeignClient(value = "semall-manage-service",configuration = FeignConfig.class)
public interface ServiceForSku {

    @RequestMapping("/checkPrice")
    boolean checkPrice(@RequestParam("productSkuId") Long productSkuId,@RequestParam("productPrice") BigDecimal productPrice);
}

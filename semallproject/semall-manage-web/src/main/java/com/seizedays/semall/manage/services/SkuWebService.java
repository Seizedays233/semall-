package com.seizedays.semall.manage.services;

import com.seizedays.semall.beans.PmsSkuInfo;
import com.seizedays.semall.webutils.configs.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient(value = "semall-manage-service",configuration = FeignConfig.class)
public interface SkuWebService {

    @RequestMapping("/service/saveSkuInfo")
    String saveSkuInfo(@RequestBody PmsSkuInfo pmsSkuInfo);

}

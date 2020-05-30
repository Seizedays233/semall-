package com.seizedays.semall.search.services;

import com.seizedays.semall.beans.PmsSearchParam;
import com.seizedays.semall.beans.PmsSearchSkuInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.seizedays.semall.webutils.configs.FeignConfig;

import java.util.List;

@FeignClient(value = "semall-search-service",configuration = FeignConfig.class)
public interface SearchWebService {

    @RequestMapping("search/service/list")
    List<PmsSearchSkuInfo> list(@RequestBody PmsSearchParam pmsSearchParam);
}

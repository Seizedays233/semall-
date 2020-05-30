package com.seizedays.semall.manage.services;

import com.seizedays.semall.beans.PmsBaseCatalog1;
import com.seizedays.semall.beans.PmsBaseCatalog2;
import com.seizedays.semall.beans.PmsBaseCatalog3;
import com.seizedays.semall.webutils.configs.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "semall-manage-service",configuration = FeignConfig.class)
public interface CatalogWebService {

    @RequestMapping("/service/getCatalog1")
    List<PmsBaseCatalog1> getCatalog1();

    @RequestMapping("/service/getCatalog2")
    List<PmsBaseCatalog2> getCatalog2(@RequestParam("catalog1Id") Long catalog1Id);

    @RequestMapping("/service/getCatalog3")
    List<PmsBaseCatalog3> getCatalog3(@RequestParam("catalog2Id") Long catalog2Id);
}

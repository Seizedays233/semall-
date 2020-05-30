package com.seizedays.semall.search.services;

import com.seizedays.semall.beans.PmsBaseAttrInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.seizedays.semall.webutils.configs.FeignConfig;

import java.util.List;
import java.util.Set;

@FeignClient(value = "semall-manage-service",configuration = FeignConfig.class)
public interface AttrSearchWebService {

    @RequestMapping("/search/service/getAttr")
    List<PmsBaseAttrInfo> getAttrValueListByValueId(@RequestBody Set<Long> valueIdSet);
}

package com.seizedays.semall.manage.services;

import com.seizedays.semall.beans.PmsBaseAttrInfo;
import com.seizedays.semall.beans.PmsBaseAttrValue;
import com.seizedays.semall.beans.PmsBaseSaleAttr;
import com.seizedays.semall.webutils.configs.FeignConfig;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(value = "semall-manage-service",configuration = FeignConfig.class)
public interface AttrWebService {

    @RequestMapping("/service/attrInfoList")
    List<PmsBaseAttrInfo> attrInfoList(@RequestParam("catalog3Id") Long catalog3Id);

    @RequestMapping("/service/saveAttrInfo")
    String saveAttrInfo(@RequestBody PmsBaseAttrInfo pmsBaseAttrInfo);

    @RequestMapping("/service/getAttrValueList")
    List<PmsBaseAttrValue> getAttrValueList(@RequestParam("attrId") Long attrId);

    @RequestMapping("/service/baseSaleAttrList")
    List<PmsBaseSaleAttr> baseSaleAttrList();
}

package com.seizedays.semall.manage.controller;

import com.seizedays.semall.beans.PmsBaseAttrInfo;
import com.seizedays.semall.beans.PmsBaseAttrValue;
import com.seizedays.semall.beans.PmsBaseSaleAttr;
import com.seizedays.semall.services.AttrInfoService;
import com.seizedays.semall.services.AttrValueService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;

@Controller
@CrossOrigin //跨域请求注解
public class AttrController {

    @Resource
    AttrInfoService attrInfoService;

    @Resource
    AttrValueService attrValueService;

    @RequestMapping("/service/attrInfoList")
    @ResponseBody
    public List<PmsBaseAttrInfo> attrInfoList(Long catalog3Id){
        return attrInfoService.getAttrInfoList(catalog3Id);
    }

    @RequestMapping("/service/saveAttrInfo")
    @ResponseBody
    public String saveAttrInfo(@RequestBody PmsBaseAttrInfo pmsBaseAttrInfo){
        String saveStatus = attrInfoService.addAttrInfo(pmsBaseAttrInfo);

        return saveStatus;
    }

    @RequestMapping("/service/getAttrValueList")
    @ResponseBody
    List<PmsBaseAttrValue> getAttrValueList(@RequestParam("attrId") Long attrId){
        return attrValueService.getAttrValueList(attrId);
    }

    @RequestMapping("/search/service/getAttr")
    @ResponseBody
    List<PmsBaseAttrInfo> getAttrValueListByValueId(@RequestBody Set<Long> valueIdSet){
        return attrInfoService.getAttrValueListByValueId(valueIdSet);
    }

    @RequestMapping("/service/baseSaleAttrList")
    @ResponseBody
    List<PmsBaseSaleAttr> baseSaleAttrList(){
        return attrInfoService.baseSaleAttrList();
    }
}

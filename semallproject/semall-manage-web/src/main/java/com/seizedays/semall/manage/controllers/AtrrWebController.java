package com.seizedays.semall.manage.controllers;

import com.seizedays.semall.beans.PmsBaseAttrInfo;
import com.seizedays.semall.beans.PmsBaseAttrValue;
import com.seizedays.semall.beans.PmsBaseSaleAttr;
import com.seizedays.semall.manage.services.AttrWebService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@CrossOrigin //跨域请求注解
public class AtrrWebController {

    @Resource
    AttrWebService attrWebService;

    @RequestMapping("/attrInfoList")
    @ResponseBody
    public List<PmsBaseAttrInfo> attrInfoList(Long catalog3Id){
        return attrWebService.attrInfoList(catalog3Id);
    }

    @RequestMapping("/saveAttrInfo")
    @ResponseBody
    public String saveAttrInfo(@RequestBody PmsBaseAttrInfo pmsBaseAttrInfo){
        String saveStatus = attrWebService.saveAttrInfo(pmsBaseAttrInfo);

        return saveStatus;
    }

    @RequestMapping("/getAttrValueList")
    @ResponseBody
    public List<PmsBaseAttrValue> getAttrValueList(@RequestParam("attrId") Long attrId){
        return attrWebService.getAttrValueList(attrId);
    }

    @RequestMapping("/baseSaleAttrList")
    @ResponseBody
    public List<PmsBaseSaleAttr> baseSaleAttrList(){
        return attrWebService.baseSaleAttrList();
    }

}

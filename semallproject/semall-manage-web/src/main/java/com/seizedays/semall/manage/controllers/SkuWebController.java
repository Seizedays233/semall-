package com.seizedays.semall.manage.controllers;

import com.seizedays.semall.beans.PmsSkuInfo;
import com.seizedays.semall.manage.services.SkuWebService;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;


@Controller
@CrossOrigin
public class SkuWebController {

    @Resource
    SkuWebService skuWebService;

    @RequestMapping(value = "/saveSkuInfo", consumes= MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String saveSkuInfo(@RequestBody PmsSkuInfo pmsSkuInfo) {

        String message = skuWebService.saveSkuInfo(pmsSkuInfo);

        return message;
    }

}

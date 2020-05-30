package com.seizedays.semall.manage.controllers;

import com.seizedays.semall.beans.PmsBaseCatalog1;
import com.seizedays.semall.beans.PmsBaseCatalog2;
import com.seizedays.semall.beans.PmsBaseCatalog3;
import com.seizedays.semall.manage.services.CatalogWebService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

@Controller
@CrossOrigin //跨域请求注解
public class CatalogWebController {

    @Resource
    CatalogWebService catalogWebService;

    @RequestMapping("/getCatalog1")
    @ResponseBody
    public List<PmsBaseCatalog1> getCatalog1(){
        List<PmsBaseCatalog1> catalog1s = catalogWebService.getCatalog1();
        return catalog1s;
    }

    @RequestMapping("/getCatalog2")
    @ResponseBody
    public List<PmsBaseCatalog2> getCatalog2(Long catalog1Id){
        return catalogWebService.getCatalog2(catalog1Id);
    }

    @RequestMapping("/getCatalog3")
    @ResponseBody
    public List<PmsBaseCatalog3> getCatalog3(Long catalog2Id){
        return catalogWebService.getCatalog3(catalog2Id);
    }

}

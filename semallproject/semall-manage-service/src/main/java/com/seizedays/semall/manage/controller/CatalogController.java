package com.seizedays.semall.manage.controller;

import com.seizedays.semall.beans.PmsBaseCatalog1;
import com.seizedays.semall.beans.PmsBaseCatalog2;
import com.seizedays.semall.beans.PmsBaseCatalog3;
import com.seizedays.semall.services.CatalogService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@Controller
@CrossOrigin //跨域请求注解
public class CatalogController {

    @Resource
    CatalogService catalogService;

    @RequestMapping("/service/getCatalog1")
    @ResponseBody
    public List<PmsBaseCatalog1> getCatalog1(){
        return catalogService.getCatalog1();
    }

    @RequestMapping("/service/getCatalog2")
    @ResponseBody
    public List<PmsBaseCatalog2> getCatalog2(Long catalog1Id){
        return catalogService.getCatalog2(catalog1Id);
    }

    @RequestMapping("/service/getCatalog3")
    @ResponseBody
    public List<PmsBaseCatalog3> getCatalog3(@RequestParam("catalog2Id") Long catalog2Id){
        return catalogService.getCatalog3(catalog2Id);
    }


}

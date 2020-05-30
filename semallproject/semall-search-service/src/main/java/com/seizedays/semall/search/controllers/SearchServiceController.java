package com.seizedays.semall.search.controllers;

import com.seizedays.semall.beans.PmsSearchParam;
import com.seizedays.semall.beans.PmsSearchSkuInfo;
import com.seizedays.semall.services.SearchService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@CrossOrigin
public class SearchServiceController {

    @Resource
    SearchService searchService;

    @RequestMapping("search/service/list")
    public List<PmsSearchSkuInfo> list(@RequestBody PmsSearchParam pmsSearchParam){
        return searchService.list(pmsSearchParam);
    }
}

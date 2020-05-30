package com.seizedays.semall.item.controller;

import com.alibaba.fastjson.JSON;
import com.seizedays.semall.beans.PmsProductSaleAttr;
import com.seizedays.semall.beans.PmsProductSaleAttrValue;
import com.seizedays.semall.beans.PmsSkuInfo;
import com.seizedays.semall.beans.PmsSkuSaleAttrValue;
import com.seizedays.semall.item.services.SkuItemWebService;
import com.seizedays.semall.item.services.SpuItemWebService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;

@Controller
@CrossOrigin
public class ItemController {

    @Resource
    SkuItemWebService skuItemWebService;

    @Resource
    SpuItemWebService spuItemWebService;

    @RequestMapping("/{skuId}.html")
    public String item(@PathVariable Long skuId, ModelMap map){
        PmsSkuInfo pmsSkuInfo = skuItemWebService.getSkuById(skuId);


        //sku对象
        map.put("skuInfo", pmsSkuInfo);

        //销售属性列表
        List<PmsProductSaleAttr> saleAttrList = spuItemWebService.spuSaleAttrListCheckBySku(pmsSkuInfo.getSpuId(), skuId);
        map.put("spuSaleAttrListCheckBySku", saleAttrList);

        //查询当前spu其他sku的集合hash表
        HashMap<String, Long> skuSaleAttrHash = new HashMap<>();

        List<PmsSkuInfo> pmsSkuInfos = skuItemWebService.getSkuSaleAttrValueListBySpu(pmsSkuInfo.getSpuId());

        for (PmsSkuInfo skuInfo : pmsSkuInfos) {
            String k = "";
            Long v = skuInfo.getId();
            List<PmsSkuSaleAttrValue> skuInfoSkuSaleAttrValueList = skuInfo.getSkuSaleAttrValueList();

            for (PmsSkuSaleAttrValue pmsSkuSaleAttrValue : skuInfoSkuSaleAttrValueList) {

                k += pmsSkuSaleAttrValue.getSaleAttrValueId() + "|";

            }

            skuSaleAttrHash.put(k, v);

        }

        //将sku的销售属性hash表放到页面
        String jsonStr = JSON.toJSONString(skuSaleAttrHash);
        System.out.println(jsonStr);
        map.put("skuSaleAttrHashJsonStr", jsonStr);

        return "item";
    }



    @RequestMapping("/itemIndex")
    public String index(){
        return "index";
    }

    @Value("${config.info}")
    private String configInfo;

    @GetMapping("/config/info1")
    @ResponseBody
    public String getConfigInfo(){
        return configInfo;
    }


}

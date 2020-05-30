package com.seizedays.semall.search.controllers;

import com.seizedays.semall.beans.*;
import com.seizedays.semall.search.services.AttrSearchWebService;
import com.seizedays.semall.search.services.SearchWebService;
import com.seizedays.semall.annotations.LoginRequired;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import java.util.*;

@Controller
@CrossOrigin
public class SearchWebController {

    @Resource
    SearchWebService searchWebService;

    @Resource
    AttrSearchWebService attrSearchWebService;

    @RequestMapping("/index")
    @LoginRequired(loginSuccessRequired = false)
    public String index() {
        return "index";
    }

    @RequestMapping("/list.html")
    public String index(PmsSearchParam pmsSearchParam, ModelMap modelMap) { //参数： 三级分类id 关键字 平台属性集合

        //调用搜索服务 返回搜索结果
        List<PmsSearchSkuInfo> pmsSearchSkuInfoList = searchWebService.list(pmsSearchParam);

        modelMap.put("skuLsInfoList", pmsSearchSkuInfoList);

        //抽取检索结果包含的平台属性集合 并去重
        Set<Long> valueIdSet = new HashSet<>();

        for (PmsSearchSkuInfo pmsSearchSkuInfo : pmsSearchSkuInfoList) {
            List<PmsSkuAttrValue> skuAttrValueList = pmsSearchSkuInfo.getSkuAttrValueList();
            for (PmsSkuAttrValue pmsSkuAttrValue : skuAttrValueList) {
                Long valueId = pmsSkuAttrValue.getValueId();
                valueIdSet.add(valueId);
            }
        }

        //根据valueId将属性的列表查询出来
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = attrSearchWebService.getAttrValueListByValueId(valueIdSet);
        modelMap.put("attrList", pmsBaseAttrInfos);

        //对平台属性进行进一步处理 去掉当前条件中valueId所在的属性组
        Long[] delValueIds = pmsSearchParam.getValueId();
        //面包屑集合
        List<PmsSearchCrumb> pmsSearchCrumbs = new ArrayList<>();
        if (null != delValueIds) {
            //将面包屑集合传给页面
            //通过迭代器进行遍历检查删除操作可以避免下标越界的问题
            for (Long delValueId : delValueIds) {
                PmsSearchCrumb pmsSearchCrumb = new PmsSearchCrumb();
                Iterator<PmsBaseAttrInfo> iterator = pmsBaseAttrInfos.iterator();

                //生成面包屑数据参数
                pmsSearchCrumb.setValueId(delValueId);
                pmsSearchCrumb.setUrlParam(getUrlParam(pmsSearchParam, delValueId));

                while (iterator.hasNext()) {
                    PmsBaseAttrInfo pmsBaseAttrInfo = iterator.next();
                    List<PmsBaseAttrValue> attrValueList = pmsBaseAttrInfo.getAttrValueList();

                    for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
                        Long valueId = pmsBaseAttrValue.getId();

                        if (delValueId == valueId) {
                            pmsSearchCrumb.setValueName(pmsBaseAttrValue.getValueName());
                            //删除该属性值所在的属性组
                            iterator.remove();
                        }
                    }
                }
                pmsSearchCrumbs.add(pmsSearchCrumb);
            }
        }

        //拼接访问路径
        String urlParam = getUrlParam(pmsSearchParam, -3L);
        modelMap.put("urlParam", urlParam);

        String keyword = pmsSearchParam.getKeyword();
        if (StringUtils.isNotBlank(keyword)) {
            modelMap.put("keyword", keyword);
        }


        modelMap.put("attrValueSelectedList", pmsSearchCrumbs);
        return "list";
    }

    //拼接访问路径
    private String getUrlParam(PmsSearchParam pmsSearchParam, Long... delValueId) {
        String keyword = pmsSearchParam.getKeyword();
        Long catalog3Id = pmsSearchParam.getCatalog3Id();
        Long[] valueIds = pmsSearchParam.getValueId();
        String urlParam = "";

        if (StringUtils.isNotBlank(keyword)) {
            urlParam += "&keyword=" + keyword;
        }

        if (null != catalog3Id) {
            urlParam += "&catalog3Id=" + catalog3Id;
        }

        if (null != valueIds && valueIds.length > 0) {

            for (Long valueId : valueIds) {
                if (null == delValueId || valueId != delValueId[0]) {
                    urlParam += "&valueId=" + valueId;
                }
            }

        }
        if (StringUtils.isNotBlank(urlParam)) {
            return urlParam.substring(1);
        } else {
            return "";
        }
    }
}

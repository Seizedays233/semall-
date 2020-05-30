package com.seizedays.semall.services;

import com.seizedays.semall.beans.PmsProductImage;
import com.seizedays.semall.beans.PmsProductInfo;
import com.seizedays.semall.beans.PmsProductSaleAttr;
import com.seizedays.semall.beans.PmsSkuInfo;

import java.util.List;

public interface SpuService {

    List<PmsProductInfo> spuList(Long catalog3Id);

    String saveSpuInfo(PmsProductInfo pmsProductInfo);

    List<PmsProductSaleAttr> spuSaleAttrList(Long spuId);

    List<PmsProductSaleAttr> spuSaleAttrList(Long spuId, Long skuId);

    List<PmsProductImage> spuImageList(Long spuId);

}

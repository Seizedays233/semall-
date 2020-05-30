package com.seizedays.semall.services;

import com.seizedays.semall.beans.PmsSkuInfo;

import java.math.BigDecimal;
import java.util.List;


public interface SkuService {

    String saveSkuInfo(PmsSkuInfo pmsSkuInfo);

    PmsSkuInfo getSkuById(Long skuId);

    List<PmsSkuInfo> getSkuSaleAttrValueListBySpu(Long spuId);

    List<PmsSkuInfo> getAllSku();

    boolean checkPrice(Long productSkuId, BigDecimal productPrice);
}

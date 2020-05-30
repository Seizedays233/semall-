package com.seizedays.semall.search.service.impl;

import com.seizedays.semall.beans.PmsSkuAttrValue;
import com.seizedays.semall.beans.PmsSkuInfo;
import com.seizedays.semall.search.mappers.PmsSkuAttrValueSearchMapper;
import com.seizedays.semall.search.mappers.PmsSkuInfoSearchMapper;
import com.seizedays.semall.services.SkuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class SkuSearchServiceImpl implements SkuService {

    @Autowired
    PmsSkuInfoSearchMapper pmsSkuInfoSearchMapper;

    @Autowired
    PmsSkuAttrValueSearchMapper pmsSkuAttrValueSearchMapper;

    @Override
    public String saveSkuInfo(PmsSkuInfo pmsSkuInfo) {
        return null;
    }

    @Override
    public PmsSkuInfo getSkuById(Long skuId) {
        return null;
    }

    @Override
    public List<PmsSkuInfo> getSkuSaleAttrValueListBySpu(Long spuId) {
        return null;
    }

    @Override
    public List<PmsSkuInfo> getAllSku() {
        List<PmsSkuInfo> pmsSkuInfoList = pmsSkuInfoSearchMapper.selectAll();
        for (PmsSkuInfo pmsSkuInfo : pmsSkuInfoList) {
            Long skuId = pmsSkuInfo.getId();

            PmsSkuAttrValue pmsSkuAttrValue = new PmsSkuAttrValue();
            pmsSkuAttrValue.setSkuId(skuId);
            List<PmsSkuAttrValue> select = pmsSkuAttrValueSearchMapper.select(pmsSkuAttrValue);

            pmsSkuInfo.setSkuAttrValueList(select);

        }
        return pmsSkuInfoList;
    }

    @Override
    public boolean checkPrice(Long productSkuId, BigDecimal productPrice) {
        return false;
    }
}

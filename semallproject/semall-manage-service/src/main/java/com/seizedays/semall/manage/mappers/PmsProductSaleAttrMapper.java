package com.seizedays.semall.manage.mappers;

import com.seizedays.semall.beans.PmsProductSaleAttr;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface PmsProductSaleAttrMapper extends Mapper<PmsProductSaleAttr> {
    List<PmsProductSaleAttr> seleceSpuSaleAttrListCheckBySku(Long spuId, Long skuId);
}

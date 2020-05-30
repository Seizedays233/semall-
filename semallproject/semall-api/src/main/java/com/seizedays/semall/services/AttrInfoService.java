package com.seizedays.semall.services;

import com.seizedays.semall.beans.PmsBaseAttrInfo;
import com.seizedays.semall.beans.PmsBaseSaleAttr;

import java.util.List;
import java.util.Set;

public interface AttrInfoService {
    List<PmsBaseAttrInfo> getAttrInfoList(Long catalog3Id);

    String addAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo);

    List<PmsBaseSaleAttr> baseSaleAttrList();

    List<PmsBaseAttrInfo> getAttrValueListByValueId(Set<Long> valueIdSet);
}

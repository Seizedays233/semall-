package com.seizedays.semall.services;

import com.seizedays.semall.beans.PmsBaseAttrInfo;
import com.seizedays.semall.beans.PmsBaseAttrValue;

import java.util.List;
import java.util.Set;

public interface AttrValueService {
    List<PmsBaseAttrValue> getAttrValueList(Long attrId);
}

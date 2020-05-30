package com.seizedays.semall.manage.service.impl;

import com.seizedays.semall.beans.PmsBaseAttrInfo;
import com.seizedays.semall.beans.PmsBaseAttrValue;
import com.seizedays.semall.manage.mappers.PmsBaseAttrInfoMapper;
import com.seizedays.semall.manage.mappers.PmsBaseAttrValueMapper;
import com.seizedays.semall.services.AttrValueService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class AttrValueServiceImpl implements AttrValueService {

    @Autowired
    PmsBaseAttrValueMapper pmsBaseAttrValueMapper;

    @Autowired
    PmsBaseAttrInfoMapper pmsBaseAttrInfoMapper;

    @Override
    public List<PmsBaseAttrValue> getAttrValueList(Long attrId) {

        PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
        pmsBaseAttrValue.setAttrId(attrId);

        return pmsBaseAttrValueMapper.select(pmsBaseAttrValue);
    }


}

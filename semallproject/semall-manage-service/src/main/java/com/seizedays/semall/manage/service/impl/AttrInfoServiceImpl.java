package com.seizedays.semall.manage.service.impl;

import com.seizedays.semall.beans.PmsBaseAttrInfo;
import com.seizedays.semall.beans.PmsBaseAttrValue;
import com.seizedays.semall.beans.PmsBaseSaleAttr;
import com.seizedays.semall.manage.mappers.PmsBaseAttrInfoMapper;
import com.seizedays.semall.manage.mappers.PmsBaseAttrValueMapper;
import com.seizedays.semall.manage.mappers.PmsBaseSaleAttrMapper;
import com.seizedays.semall.services.AttrInfoService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;
import java.util.Set;

@Service
public class AttrInfoServiceImpl implements AttrInfoService {

    @Autowired
    PmsBaseAttrInfoMapper pmsBaseAttrInfoMapper;

    @Autowired
    PmsBaseAttrValueMapper pmsBaseAttrValueMapper;

    @Autowired
    PmsBaseSaleAttrMapper pmsBaseSaleAttrMapper;

    @Override
    public List<PmsBaseAttrInfo> getAttrInfoList(Long catalog3Id) {

        PmsBaseAttrInfo pmsBaseAttrInfo = new PmsBaseAttrInfo();
        pmsBaseAttrInfo.setCatalog3Id(catalog3Id);

        List<PmsBaseAttrInfo> attrInfoList =  pmsBaseAttrInfoMapper.select(pmsBaseAttrInfo);

        for (PmsBaseAttrInfo baseAttrInfo : attrInfoList) {

            PmsBaseAttrValue pmsBaseAttrValue = new PmsBaseAttrValue();
            pmsBaseAttrValue.setAttrId(baseAttrInfo.getId());
            List<PmsBaseAttrValue> attrValueList = pmsBaseAttrValueMapper.select(pmsBaseAttrValue);

            baseAttrInfo.setAttrValueList(attrValueList);
        }

        return attrInfoList;
    }

    @Override
    public String addAttrInfo(PmsBaseAttrInfo pmsBaseAttrInfo) {

        Long id = pmsBaseAttrInfo.getId();
        //属性值列表
        List<PmsBaseAttrValue> attrValueList = null;
        //非空判断
        if (null == id){
            //保存属性
            pmsBaseAttrInfoMapper.insertSelective(pmsBaseAttrInfo); //insertSelective 只将有值的 非空的数据插入到数据库

            // 给属性值列表赋值
            attrValueList = pmsBaseAttrInfo.getAttrValueList();
        }else {
            //id非空 执行属性的修改更新操作
            Example example = new Example(PmsBaseAttrInfo.class);
            example.createCriteria().andEqualTo("id", pmsBaseAttrInfo.getId());
            //执行修改 将pmsBaseAttrInfo的属性修改到example中
            pmsBaseAttrInfoMapper.updateByExampleSelective(pmsBaseAttrInfo, example);

            //修改属性的值

            //先按照属性id清空原有的pmsBaseAttrValue 再整体进行添加
            PmsBaseAttrValue pmsBaseAttrValueDel = new PmsBaseAttrValue();
            pmsBaseAttrValueDel.setAttrId(pmsBaseAttrInfo.getId());
            pmsBaseAttrValueMapper.delete(pmsBaseAttrValueDel);

           // 给属性值列表赋值
            attrValueList = pmsBaseAttrInfo.getAttrValueList();
        }

        //保存属性值
        for (PmsBaseAttrValue pmsBaseAttrValue : attrValueList) {
            pmsBaseAttrValue.setAttrId(pmsBaseAttrInfo.getId());

            pmsBaseAttrValueMapper.insertSelective(pmsBaseAttrValue);
        }
        return "success";
    }

    @Override
    public List<PmsBaseSaleAttr> baseSaleAttrList() {
        return pmsBaseSaleAttrMapper.selectAll();
    }

    @Override
    public List<PmsBaseAttrInfo> getAttrValueListByValueId(Set<Long> valueIdSet) {
        String valueIdStr = StringUtils.join(valueIdSet, ",");  // 41,45,46
        System.out.println(valueIdStr);
        List<PmsBaseAttrInfo> pmsBaseAttrInfos = pmsBaseAttrInfoMapper.selectAttrValueListByValueId(valueIdStr);
        return pmsBaseAttrInfos;
    }
}

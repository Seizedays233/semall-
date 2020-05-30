package com.seizedays.semall.beans;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

@Data
public class PmsSkuAttrValue implements Serializable {

    @Id
    @Column
    Long id;

    //平台属性 PmsBaseSaleAttrInfo
    @Column
    Long attrId;

    //平台属性值 PmsBaseAttrValue
    @Column
    Long valueId;

    //商品id
    @Column
    Long skuId;


}

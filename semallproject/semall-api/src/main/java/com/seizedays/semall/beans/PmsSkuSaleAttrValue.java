package com.seizedays.semall.beans;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

/**
 * @param
 * @return
 */
@Data
public class PmsSkuSaleAttrValue implements Serializable {

    @Id
    @Column
    Long id;

    @Column
    Long skuId;

    @Column
    Long saleAttrId;

    @Column
    Long saleAttrValueId;

    @Column
    String saleAttrName;

    @Column
    String saleAttrValueName;

    }

package com.seizedays.semall.beans;

import lombok.Data;

import javax.persistence.Id;
import java.math.BigDecimal;
import java.util.List;

@Data
public class PmsSearchSkuInfo {
    @Id
    private Long id;
    private String skuName;
    private String skuDesc;
    private Long catalog3Id;
    private BigDecimal price;
    private String skuDefaultImg;
    private Long spuId;
    private Double hotScore;
    private List<PmsSkuAttrValue> skuAttrValueList;
}

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
public class PmsSkuImage implements Serializable {

    @Id
    @Column
    Long id;
    @Column
    Long skuId;
    @Column
    String imgName;
    @Column
    String imgUrl;
    @Column(name = "product_img_id")
    Long spuImgId;
    @Column
    String isDefault;


}

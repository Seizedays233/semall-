package com.seizedays.semall.beans;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.io.Serializable;

@Data
public class PmsProductSaleAttrValue implements Serializable {
    @Id
    @Column
    Long id ;

    @Column
    Long productId;

    @Column
    Long saleAttrId;

    @Column
    String saleAttrValueName;

    @Transient
    String isChecked;


}

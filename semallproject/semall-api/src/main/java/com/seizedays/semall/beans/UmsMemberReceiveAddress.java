package com.seizedays.semall.beans;

import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;

@Data
public class UmsMemberReceiveAddress implements Serializable {

    @Id
    private Long id;
    private Long memberId;
    private String  name;
    private String  phoneNumber;
    private Integer defaultStatus;
    private String postCode;
    private String province;
    private String city;
    private String region;
    private String detailAddress;

   }

package com.seizedays.semall.beans;

import lombok.Data;

import java.util.List;

@Data
public class PmsSearchParam {
    private String keyword;
    private Long catalog3Id;
    private Long[] valueId;

}

package com.seizedays.semall.beans;

import lombok.Data;

import javax.persistence.Id;
import java.io.Serializable;

@Data
public class PmsBrand  implements Serializable {

    @Id
    private Long id;
    private String name;
    private String firstLetter;
    private int sort;
    private int factoryStatus;
    private int showStatus;
    private int productCount;
    private String productCommentCount;
    private String logo;
    private String bigPic;
    private String brandStory;

   }

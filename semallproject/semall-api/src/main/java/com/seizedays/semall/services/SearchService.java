package com.seizedays.semall.services;

import com.seizedays.semall.beans.PmsSearchParam;
import com.seizedays.semall.beans.PmsSearchSkuInfo;

import java.util.List;

public interface SearchService {
    List<PmsSearchSkuInfo> list(PmsSearchParam pmsSearchParam);
}

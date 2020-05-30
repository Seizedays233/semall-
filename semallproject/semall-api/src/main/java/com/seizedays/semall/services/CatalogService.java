package com.seizedays.semall.services;

import com.seizedays.semall.beans.PmsBaseCatalog1;
import com.seizedays.semall.beans.PmsBaseCatalog2;
import com.seizedays.semall.beans.PmsBaseCatalog3;

import java.util.List;

public interface CatalogService {
    List<PmsBaseCatalog1> getCatalog1();

    List<PmsBaseCatalog2> getCatalog2(Long catalog1Id);

    List<PmsBaseCatalog3> getCatalog3(Long catalog2Id);
}

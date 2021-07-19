package com.assignment.warehouse.service;

import com.assignment.warehouse.model.AvailableProduct;
import com.assignment.warehouse.model.PurchaseProduct;
import com.assignment.warehouse.model.PurchaseResponse;

import java.util.List;

public interface AvailableProductService {

    List<AvailableProduct> findAll();

    PurchaseResponse sell(List<PurchaseProduct> items);
}

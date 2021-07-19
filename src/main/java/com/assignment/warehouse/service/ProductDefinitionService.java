package com.assignment.warehouse.service;

import com.assignment.warehouse.model.Product;
import com.assignment.warehouse.model.Products;

public interface ProductDefinitionService {

    Products findAll();

    Product findOne(String name);

    void deleteOne(String name);
}

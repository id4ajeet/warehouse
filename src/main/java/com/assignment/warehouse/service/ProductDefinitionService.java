package com.assignment.warehouse.service;

import com.assignment.warehouse.model.Product;
import com.assignment.warehouse.model.Products;

public interface ProductDefinitionService {

    Products findAll();

    Product findOne(final String name);

    void deleteOne(final String name);
}

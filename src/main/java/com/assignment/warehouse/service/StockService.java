package com.assignment.warehouse.service;

import com.assignment.warehouse.model.Article;
import com.assignment.warehouse.model.Inventory;

public interface StockService {
    Inventory findAll();

    Article findOne(final String id);

    void deleteOne(final String id);
}

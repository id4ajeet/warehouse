package com.assignment.warehouse.service;

import com.assignment.warehouse.model.Article;
import com.assignment.warehouse.model.Inventory;

import javax.transaction.Transactional;

public interface StockService {
    Inventory findAll();

    Article findOne(String id);

    @Transactional
    void deleteOne(String id);
}

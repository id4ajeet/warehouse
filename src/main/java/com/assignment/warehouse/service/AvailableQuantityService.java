package com.assignment.warehouse.service;

import com.assignment.warehouse.model.ProductSubArticle;

import java.util.List;
import java.util.Map;

public interface AvailableQuantityService {

    long calculate(List<ProductSubArticle> parts);

    void updateStock(Map<String, Long> articlesNeeded);
}

package com.assignment.warehouse.service;

import com.assignment.warehouse.model.ProductSubArticle;

import java.util.List;
import java.util.Map;

public interface AvailableQuantityService {

    long calculate(final List<ProductSubArticle> parts);

    void updateStock(final Map<String, Long> articlesNeeded);
}

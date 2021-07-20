package com.assignment.warehouse.service.impl;

import com.assignment.warehouse.db.repository.InventoryRepository;
import com.assignment.warehouse.exception.StockNotAvailable;
import com.assignment.warehouse.model.ProductSubArticle;
import com.assignment.warehouse.service.AvailableQuantityService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class AvailableQuantityServiceImpl implements AvailableQuantityService {

    private static final long ZERO = 0L;

    @NonNull
    private final InventoryRepository inventoryRepository;

    @Override
    public long calculate(final List<ProductSubArticle> parts) {
        return parts.stream()
                .filter(article -> Long.parseLong(article.getArticleCount()) > ZERO)
                .mapToLong(this::calculate)
                .min()
                .orElse(ZERO);
    }

    @Override
    @Transactional
    public void updateStock(final Map<String, Long> articlesNeeded) {

        //fetch required Articles
        var entities = articlesNeeded.keySet()
                .stream()
                .map(key -> inventoryRepository.findById(key).orElseThrow(() -> new StockNotAvailable(String.format("Article [%s] not found", key))))
                .collect(Collectors.toList());

        //Check Availability
        entities.stream()
                .filter(entity -> Long.parseLong(entity.getStock()) < articlesNeeded.get(entity.getId()))
                .findAny()
                .ifPresent(entity -> {
                    throw new StockNotAvailable(String.format("Stock not available for article[%s] - %s", entity.getId(), entity.getName()));
                });

        //Update stock
        entities.forEach(entity -> {
            long newStock = Long.parseLong(entity.getStock()) - articlesNeeded.get(entity.getId());
            entity.setStock(String.valueOf(newStock));
        });

        //Save in DB
        inventoryRepository.saveAll(entities);
    }

    private long calculate(final ProductSubArticle article) {
        return inventoryRepository.findById(article.getArticleId())
                .map(entity -> Long.parseLong(entity.getStock()) / Long.parseLong(article.getArticleCount()))
                .orElse(ZERO);
    }
}

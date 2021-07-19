package com.assignment.warehouse.service.impl;

import com.assignment.warehouse.db.entity.ArticleEntity;
import com.assignment.warehouse.db.repository.InventoryRepository;
import com.assignment.warehouse.exception.ArticleNotFound;
import com.assignment.warehouse.model.Article;
import com.assignment.warehouse.model.Inventory;
import com.assignment.warehouse.service.StockService;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.converter.Converter;

import javax.transaction.Transactional;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class StockServiceImpl implements StockService {

    @NonNull
    private final InventoryRepository inventoryRepository;

    @NonNull
    private final Converter<ArticleEntity, Article> articleEntityArticleConverter;

    @Override
    public Inventory findAll() {
        var articles = inventoryRepository.findAll()
                .stream()
                .map(articleEntityArticleConverter::convert)
                .collect(Collectors.toList());

        if (articles.isEmpty()) {
            throw new ArticleNotFound("Stock is empty");
        }

        var inventory = new Inventory();
        inventory.setArticles(articles);
        return inventory;
    }

    @Override
    public Article findOne(String id) {
        return inventoryRepository.findById(id)
                .map(articleEntityArticleConverter::convert)
                .orElseThrow(() -> new ArticleNotFound(String.format("Article[%s] not found", id)));
    }

    @Override
    @Transactional
    public void deleteOne(String id) {
        inventoryRepository.findById(id)
                .ifPresent(inventoryRepository::delete);
    }
}

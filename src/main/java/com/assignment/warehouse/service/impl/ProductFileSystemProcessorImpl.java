package com.assignment.warehouse.service.impl;

import com.assignment.warehouse.db.entity.ProductEntity;
import com.assignment.warehouse.db.entity.ProductSubArticleEntity;
import com.assignment.warehouse.db.repository.ProductRepository;
import com.assignment.warehouse.model.Product;
import com.assignment.warehouse.model.ProductSubArticle;
import com.assignment.warehouse.model.Products;
import com.assignment.warehouse.service.InputDataProcessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class ProductFileSystemProcessorImpl implements InputDataProcessor {
    @NonNull
    private final ObjectMapper objectMapper;

    @NonNull
    private final ProductRepository productRepository;

    @Override
    public void run(String filePath) {
        try {
            var products = objectMapper.readValue(Path.of(filePath).toFile(), Products.class);

            Optional.ofNullable(products)
                    .ifPresentOrElse(this::saveInDB, () -> log.info("No product present to load in db"));

        } catch (IOException e) {
            log.error("failed to process file {}", filePath, e);
        }
    }

    @Transactional
    public void saveInDB(Products products) {
        if (Objects.isNull(products.getItems())) {
            log.info("No product present to process");
            return;
        }

        var dbProducts = products.getItems()
                .stream()
                .map(this::convertToProductEntity)
                .collect(Collectors.toList());

        productRepository.saveAll(dbProducts);
        log.info("DB updated with products size={}", dbProducts.size());
    }

    private ProductEntity convertToProductEntity(Product product) {
        return productRepository.findByName(product.getName())
                .map(dbEntity -> updateProduct(dbEntity, product))
                .orElseGet(() -> createProduct(product));
    }

    private ProductEntity createProduct(Product product) {

        var dbEntity = new ProductEntity();
        dbEntity.setName(product.getName());
        dbEntity.setPrice(product.getPrice());

        product.getParts()
                .stream()
                .map(this::convertToProductSubArticle)
                .forEach(dbEntity.getSubArticles()::add);

        return dbEntity;
    }

    private ProductEntity updateProduct(ProductEntity dbEntity, Product product) {

        var subArticles = dbEntity.getSubArticles();

        var dbArticlesIds = subArticles.stream()
                .collect(Collectors.toMap(ProductSubArticleEntity::getArticleId, Function.identity()));

        var articlesIds = product.getParts()
                .stream()
                .map(ProductSubArticle::getArticleId)
                .collect(Collectors.toSet());

        //Remove articles which are not in definition
        dbArticlesIds.entrySet().stream()
                .filter(article -> !articlesIds.contains(article.getKey()))
                .forEach(article -> subArticles.remove(article.getValue()));

        //Add new articles
        product.getParts().stream()
                .filter(p -> !dbArticlesIds.containsKey(p.getArticleId()))
                .map(this::convertToProductSubArticle)
                .forEach(subArticles::add);

        //Update Existing Article
        product.getParts().stream()
                .filter(p -> dbArticlesIds.containsKey(p.getArticleId()))
                .forEach(p -> {
                    var entity = dbArticlesIds.get(p.getArticleId());
                    entity.setArticleQuantity(p.getArticleCount());
                });

        dbEntity.setSubArticles(subArticles);
        dbEntity.setPrice(product.getPrice());
        return dbEntity;
    }

    private ProductSubArticleEntity convertToProductSubArticle(ProductSubArticle p) {
        var entity = new ProductSubArticleEntity();
        entity.setArticleId(p.getArticleId());
        entity.setArticleQuantity(p.getArticleCount());
        return entity;
    }
}

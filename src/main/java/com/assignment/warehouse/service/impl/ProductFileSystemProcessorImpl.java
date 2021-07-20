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
    public void run(final String filePath) {
        try {
            final var products = objectMapper.readValue(Path.of(filePath).toFile(), Products.class);

            Optional.ofNullable(products)
                    .ifPresentOrElse(this::saveInDB, () -> log.info("No product present to load in db"));

        } catch (IOException e) {
            log.error("failed to process file {}", filePath, e);
        }
    }

    @Transactional
    public void saveInDB(final Products products) {
        if (Objects.isNull(products.getItems())) {
            log.info("No product present to process");
            return;
        }

        final var dbProducts = products.getItems()
                .stream()
                .map(this::convertToProductEntity)
                .collect(Collectors.toList());

        productRepository.saveAll(dbProducts);
        log.info("DB updated with products size={}", dbProducts.size());
    }

    private ProductEntity convertToProductEntity(final Product product) {
        return productRepository.findByName(product.getName())
                .map(dbEntity -> updateProduct(dbEntity, product))
                .orElseGet(() -> createProduct(product));
    }

    private ProductEntity createProduct(final Product product) {

        var dbEntity = new ProductEntity();
        dbEntity.setName(product.getName());
        dbEntity.setPrice(product.getPrice());

        product.getParts()
                .stream()
                .map(this::convertToProductSubArticle)
                .forEach(dbEntity.getSubArticles()::add);

        return dbEntity;
    }

    private ProductEntity updateProduct(final ProductEntity dbEntity, final Product product) {

        final var subArticles = dbEntity.getSubArticles();

        final var dbArticlesIds = subArticles.stream()
                .collect(Collectors.toMap(ProductSubArticleEntity::getArticleId, Function.identity()));

        final var articlesIds = product.getParts()
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

    private ProductSubArticleEntity convertToProductSubArticle(final ProductSubArticle article) {
        var entity = new ProductSubArticleEntity();
        entity.setArticleId(article.getArticleId());
        entity.setArticleQuantity(article.getArticleCount());
        return entity;
    }
}

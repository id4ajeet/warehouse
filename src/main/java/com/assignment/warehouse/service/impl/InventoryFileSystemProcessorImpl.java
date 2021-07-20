package com.assignment.warehouse.service.impl;

import com.assignment.warehouse.db.entity.ArticleEntity;
import com.assignment.warehouse.db.repository.InventoryRepository;
import com.assignment.warehouse.model.Article;
import com.assignment.warehouse.model.Inventory;
import com.assignment.warehouse.service.InputDataProcessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.transaction.Transactional;
import java.nio.file.Path;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@AllArgsConstructor
public class InventoryFileSystemProcessorImpl implements InputDataProcessor {
    @NonNull
    private final ObjectMapper objectMapper;

    @NonNull
    private final InventoryRepository inventoryRepository;

    @Override
    public void run(final String filePath) {
        try {
            final var inventory = objectMapper.readValue(Path.of(filePath).toFile(), Inventory.class);

            Optional.ofNullable(inventory)
                    .ifPresentOrElse(this::saveInDB, () -> log.info("No article present to load in db"));

        } catch (Exception e) {
            log.error("failed to process file {}", filePath, e);
        }
    }

    @Transactional
    public void saveInDB(final Inventory inv) {
        if (Objects.isNull(inv.getArticles())) {
            log.info("No article present to process");
            return;
        }

        final var articles = inv.getArticles()
                .stream()
                .map(this::convertToArticleEntity)
                .collect(Collectors.toList());

        inventoryRepository.saveAll(articles);

        log.info("DB updated with articles size={}", articles.size());
    }

    private ArticleEntity convertToArticleEntity(final Article article) {
        return inventoryRepository.findById(article.getId())
                .map(dbEntity -> updateArticle(dbEntity, article))
                .orElseGet(() -> createArticle(article));
    }

    private ArticleEntity createArticle(final Article article) {
        var dbArticle = new ArticleEntity();
        dbArticle.setId(article.getId());
        dbArticle.setName(article.getName());
        dbArticle.setStock(article.getStock());
        return dbArticle;
    }

    private ArticleEntity updateArticle(final ArticleEntity dbArticle, final Article article) {
        dbArticle.setName(article.getName());
        dbArticle.setStock(article.getStock());
        return dbArticle;
    }
}

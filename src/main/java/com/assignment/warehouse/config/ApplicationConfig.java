package com.assignment.warehouse.config;

import com.assignment.warehouse.db.entity.ArticleEntity;
import com.assignment.warehouse.db.entity.ProductEntity;
import com.assignment.warehouse.db.entity.ProductSubArticleEntity;
import com.assignment.warehouse.db.mapper.ArticleEntityToArticleConverter;
import com.assignment.warehouse.db.mapper.ProductEntityToAvailableProductConverter;
import com.assignment.warehouse.db.mapper.ProductEntityToProductConverter;
import com.assignment.warehouse.db.mapper.ProductSubArticleEntityEntityToProductSubArticleEntity;
import com.assignment.warehouse.db.repository.InventoryRepository;
import com.assignment.warehouse.db.repository.ProductRepository;
import com.assignment.warehouse.model.Article;
import com.assignment.warehouse.model.AvailableProduct;
import com.assignment.warehouse.model.Product;
import com.assignment.warehouse.model.ProductSubArticle;
import com.assignment.warehouse.service.AvailableProductService;
import com.assignment.warehouse.service.AvailableQuantityService;
import com.assignment.warehouse.service.ProductDefinitionService;
import com.assignment.warehouse.service.StockService;
import com.assignment.warehouse.service.impl.AvailableProductServiceImpl;
import com.assignment.warehouse.service.impl.AvailableQuantityServiceImpl;
import com.assignment.warehouse.service.impl.ProductDefinitionServiceImpl;
import com.assignment.warehouse.service.impl.StockServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;

@Configuration
public class ApplicationConfig {

    @Bean
    public Converter<ArticleEntity, Article> articleEntityArticleConverter() {
        return new ArticleEntityToArticleConverter();
    }

    @Bean
    public Converter<ProductEntity, Product> productEntityProductConverter(final Converter<ProductSubArticleEntity, ProductSubArticle> productSubArticleEntityProductSubArticleConverter) {
        return new ProductEntityToProductConverter(productSubArticleEntityProductSubArticleConverter);
    }

    @Bean
    public Converter<ProductEntity, AvailableProduct> productEntityAvailableProductConverter(final Converter<ProductSubArticleEntity, ProductSubArticle> productSubArticleEntityProductSubArticleConverter) {
        return new ProductEntityToAvailableProductConverter(productSubArticleEntityProductSubArticleConverter);
    }

    @Bean
    public Converter<ProductSubArticleEntity, ProductSubArticle> productSubArticleEntityProductSubArticleConverter() {
        return new ProductSubArticleEntityEntityToProductSubArticleEntity();
    }

    @Bean
    public StockService stockService(final InventoryRepository inventoryRepository,
                                     final Converter<ArticleEntity, Article> articleEntityArticleConverter) {
        return new StockServiceImpl(inventoryRepository, articleEntityArticleConverter);
    }

    @Bean
    public ProductDefinitionService productDefinitionService(final ProductRepository productRepository,
                                                             final Converter<ProductEntity, Product> productEntityProductConverter) {
        return new ProductDefinitionServiceImpl(productRepository, productEntityProductConverter);
    }

    @Bean
    public AvailableQuantityService quantityService(final InventoryRepository inventoryRepository) {
        return new AvailableQuantityServiceImpl(inventoryRepository);
    }

    @Bean
    public AvailableProductService availableProductService(final ProductRepository productRepository,
                                                           final AvailableQuantityService quantityService,
                                                           final Converter<ProductEntity, AvailableProduct> productEntityAvailableProductConverter) {
        return new AvailableProductServiceImpl(productRepository, quantityService, productEntityAvailableProductConverter);
    }
}

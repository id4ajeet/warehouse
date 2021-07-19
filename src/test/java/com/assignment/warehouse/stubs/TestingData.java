package com.assignment.warehouse.stubs;

import com.assignment.warehouse.db.entity.ArticleEntity;
import com.assignment.warehouse.db.entity.ProductEntity;
import com.assignment.warehouse.db.entity.ProductSubArticleEntity;
import com.assignment.warehouse.model.ProductSubArticle;
import com.assignment.warehouse.model.PurchaseProduct;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TestingData {

    public static ProductSubArticleEntity stubProductSubArticleEntity(long productId, String articleId, String quantity) {
        ProductSubArticleEntity entity = new ProductSubArticleEntity();
        entity.setProductId(productId);
        entity.setArticleId(articleId);
        entity.setArticleQuantity(quantity);
        return entity;
    }

    public static ProductEntity stubProductEntity(long id, String name, String articleId1, String quantity1, String articleId2, String quantity2, String price) {
        var entity = new ProductEntity();
        entity.setId(id);
        entity.setName(name);
        entity.setPrice(price);

        List<ProductSubArticleEntity> subArticles = new ArrayList<>();
        subArticles.add(stubProductSubArticleEntity(id, articleId1, quantity1));
        subArticles.add(stubProductSubArticleEntity(id, articleId2, quantity2));

        entity.setSubArticles(subArticles);
        return entity;
    }

    public static PurchaseProduct stubPurchaseProduct(String name, long quantity) {
        var product = new PurchaseProduct();
        product.setName(name);
        product.setQuantity(quantity);
        return product;
    }

    public static ArticleEntity stubArticleEntity(String id, String name, String stock) {
        ArticleEntity entity = new ArticleEntity();
        entity.setId(id);
        entity.setName(name);
        entity.setStock(stock);
        return entity;
    }

    public static Optional<ArticleEntity> stubOptArticleEntity(String id, String name, String stock) {
        return Optional.of(stubArticleEntity(id, name, stock));
    }


    public static ProductSubArticle stubSubArticle(String articleId, String articleCount) {
        ProductSubArticle part = new ProductSubArticle();
        part.setArticleId(articleId);
        part.setArticleCount(articleCount);
        return part;
    }
}

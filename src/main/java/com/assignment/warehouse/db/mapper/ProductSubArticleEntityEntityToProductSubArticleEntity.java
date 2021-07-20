package com.assignment.warehouse.db.mapper;

import com.assignment.warehouse.db.entity.ProductSubArticleEntity;
import com.assignment.warehouse.model.ProductSubArticle;
import org.springframework.core.convert.converter.Converter;

public class ProductSubArticleEntityEntityToProductSubArticleEntity implements Converter<ProductSubArticleEntity, ProductSubArticle> {

    @Override
    public ProductSubArticle convert(final ProductSubArticleEntity entity) {
        var article = new ProductSubArticle();
        article.setArticleId(entity.getArticleId());
        article.setArticleCount(entity.getArticleQuantity());
        return article;
    }
}

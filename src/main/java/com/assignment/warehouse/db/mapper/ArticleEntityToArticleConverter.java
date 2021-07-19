package com.assignment.warehouse.db.mapper;

import com.assignment.warehouse.db.entity.ArticleEntity;
import com.assignment.warehouse.model.Article;
import org.springframework.core.convert.converter.Converter;

public class ArticleEntityToArticleConverter implements Converter<ArticleEntity, Article> {

    @Override
    public Article convert(ArticleEntity articleEntity) {
        var article = new Article();
        article.setId(articleEntity.getId());
        article.setName(articleEntity.getName());
        article.setStock(articleEntity.getStock());
        return article;
    }
}

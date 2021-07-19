package com.assignment.warehouse.db.mapper;

import com.assignment.warehouse.db.entity.ArticleEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.assignment.warehouse.stubs.TestingData.stubArticleEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ArticleEntityToArticleConverterTest {

    private ArticleEntityToArticleConverter converter;

    @BeforeEach
    void setUp() {
        converter = new ArticleEntityToArticleConverter();
    }

    @Test
    void convert() {
        //GIVEN
        ArticleEntity entity = stubArticleEntity("1", "Article1", "10");

        //WHEN
        var article = converter.convert(entity);

        //THEN
        assertEquals("1", article.getId());
        assertEquals("Article1", article.getName());
        assertEquals("10", article.getStock());
    }
}
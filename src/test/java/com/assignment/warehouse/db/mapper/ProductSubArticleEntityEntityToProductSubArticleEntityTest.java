package com.assignment.warehouse.db.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.assignment.warehouse.stubs.TestingData.stubProductSubArticleEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductSubArticleEntityEntityToProductSubArticleEntityTest {

    private ProductSubArticleEntityEntityToProductSubArticleEntity converter;

    @BeforeEach
    void setUp() {
        converter = new ProductSubArticleEntityEntityToProductSubArticleEntity();
    }

    @Test
    void convert() {
        //GIVEN
        var entity = stubProductSubArticleEntity(1l, "2", "10");

        //WHEN
        var article = converter.convert(entity);

        //THEN
        assertEquals("2", article.getArticleId());
        assertEquals("10", article.getArticleCount());
    }
}
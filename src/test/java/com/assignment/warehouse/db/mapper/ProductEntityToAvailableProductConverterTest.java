package com.assignment.warehouse.db.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.assignment.warehouse.stubs.TestingData.stubProductEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductEntityToAvailableProductConverterTest {

    private ProductEntityToAvailableProductConverter converter;

    @BeforeEach
    void setUp() {
        converter = new ProductEntityToAvailableProductConverter(new ProductSubArticleEntityEntityToProductSubArticleEntity());
    }

    @Test
    void convert() {
        //GIVEN
        var entity = stubProductEntity(1l, "Fan", "1", "5", "2", "2", "100");

        //WHEN
        var product = converter.convert(entity);

        //THEN
        assertEquals("Fan", product.getName());
        assertEquals("100", product.getPrice());
        assertEquals(0, product.getQuantity());
        assertEquals(2, product.getParts().size());
    }
}

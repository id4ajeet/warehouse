package com.assignment.warehouse.service.impl;

import com.assignment.warehouse.db.entity.ProductEntity;
import com.assignment.warehouse.db.mapper.ProductEntityToProductConverter;
import com.assignment.warehouse.db.mapper.ProductSubArticleEntityEntityToProductSubArticleEntity;
import com.assignment.warehouse.db.repository.ProductRepository;
import com.assignment.warehouse.exception.ProductNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.assignment.warehouse.stubs.TestingData.stubProductEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductDefinitionServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    private ProductDefinitionServiceImpl productDefinitionService;

    @BeforeEach
    void setUp() {
        var converter = new ProductEntityToProductConverter(new ProductSubArticleEntityEntityToProductSubArticleEntity());
        productDefinitionService = new ProductDefinitionServiceImpl(productRepository, converter);
    }

    @Test
    void findAllSuccess() {
        //GIVEN
        List<ProductEntity> entities = new ArrayList<>();
        entities.add(stubProductEntity(1l, "Fan", "1", "5", "2", "2", "100"));
        entities.add(stubProductEntity(2l, "Table", "1", "5", "3", "2", "100"));
        when(productRepository.findAll()).thenReturn(entities);

        //WHEN
        var products = productDefinitionService.findAll();

        //THEN
        assertNotNull(products);
        assertEquals(2, products.getItems().size());
        assertEquals("Fan", products.getItems().get(0).getName());
        assertEquals("100", products.getItems().get(0).getPrice());
        assertEquals(2, products.getItems().get(0).getParts().size());
    }

    @Test
    void findAllFailureNothingInDb() {
        //GIVEN
        when(productRepository.findAll()).thenReturn(Collections.emptyList());

        //WHEN
        var ex = assertThrows(ProductNotFound.class, () -> productDefinitionService.findAll());

        //THEN
        assertEquals("No product present in warehouse", ex.getMessage());
    }

    @Test
    void findOneSuccess() {
        //GIVEN
        var entity = stubProductEntity(1l, "Fan", "1", "5", "2", "2", "100");
        when(productRepository.findByName("Fan")).thenReturn(Optional.of(entity));

        //WHEN
        var product = productDefinitionService.findOne("Fan");

        //THEN
        assertNotNull(product);
        assertEquals("Fan", product.getName());
        assertEquals("100", product.getPrice());
        assertEquals(2, product.getParts().size());
    }

    @Test
    void findOneFailure() {
        //GIVEN
        when(productRepository.findByName("Fan")).thenReturn(Optional.empty());

        //WHEN
        var ex = assertThrows(ProductNotFound.class, () -> productDefinitionService.findOne("Fan"));

        //THEN
        assertEquals("Product[Fan] not found", ex.getMessage());
    }

    @Test
    void deleteOneSuccess() {
        //GIVEN
        var entity = stubProductEntity(1l, "Fan", "1", "5", "2", "2", "100");
        when(productRepository.findByName("Fan")).thenReturn(Optional.of(entity));

        //WHEN
        productDefinitionService.deleteOne("Fan");

        //THEN
        verify(productRepository, times(1)).delete(entity);
    }

    @Test
    void deleteOneFailure() {
        //GIVEN
        when(productRepository.findByName("Fan")).thenReturn(Optional.empty());

        //WHEN
        productDefinitionService.deleteOne("Fan");

        //THEN
        verify(productRepository, never()).delete(any());
    }
}
package com.assignment.warehouse.service.impl;

import com.assignment.warehouse.db.entity.ProductEntity;
import com.assignment.warehouse.db.mapper.ProductEntityToAvailableProductConverter;
import com.assignment.warehouse.db.mapper.ProductSubArticleEntityEntityToProductSubArticleEntity;
import com.assignment.warehouse.db.repository.ProductRepository;
import com.assignment.warehouse.exception.StockNotAvailable;
import com.assignment.warehouse.model.PurchaseProduct;
import com.assignment.warehouse.service.AvailableQuantityService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.assignment.warehouse.stubs.TestingData.stubProductEntity;
import static com.assignment.warehouse.stubs.TestingData.stubPurchaseProduct;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AvailableProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private AvailableQuantityService quantityService;

    private ProductEntityToAvailableProductConverter productEntityAvailableProductConverter;

    private AvailableProductServiceImpl availableProductService;

    @BeforeEach
    void setUp() {
        productEntityAvailableProductConverter = new ProductEntityToAvailableProductConverter(new ProductSubArticleEntityEntityToProductSubArticleEntity());
        availableProductService = new AvailableProductServiceImpl(productRepository, quantityService, productEntityAvailableProductConverter);
    }

    @Test
    void findAllSuccess() {
        //GIVEN
        List<ProductEntity> entities = new ArrayList<>();
        entities.add(stubProductEntity(1l, "Fan", "1", "5", "2", "2", "100"));
        entities.add(stubProductEntity(2l, "Fan", "1", "5", "3", "2", "100"));
        when(productRepository.findAll()).thenReturn(entities);
        when(quantityService.calculate(any())).thenReturn(2l);

        //WHEN
        var products = availableProductService.findAll();

        //THEN
        assertNotNull(products);
        assertEquals(2, products.size());
        assertEquals(2l, products.get(0).getQuantity());
    }

    @Test
    void findAllFailWhenNoProductAvailable() {
        //GIVEN
        List<ProductEntity> entities = new ArrayList<>();
        entities.add(stubProductEntity(1l, "Product1", "1", "5", "2", "2", "100"));
        entities.add(stubProductEntity(2l, "Product2", "1", "5", "3", "2", "100"));
        when(productRepository.findAll()).thenReturn(entities);
        when(quantityService.calculate(any())).thenReturn(0l);

        //WHEN
        var ex = assertThrows(StockNotAvailable.class, () -> availableProductService.findAll());

        //THEN
        verify(productRepository, times(1)).findAll();
        assertEquals("No product available", ex.getMessage());
    }

    @Test
    void sellSuccess() {
        //GIVEN
        List<PurchaseProduct> items = new ArrayList<>();
        items.add(stubPurchaseProduct("Product1", 5L));
        items.add(stubPurchaseProduct("Product2", 2L));

        var product1 = stubProductEntity(1l, "Product1", "1", "5", "2", "2", "100");
        when(productRepository.findByName("Product1")).thenReturn(Optional.of(product1));

        var product2 = stubProductEntity(2l, "Product2", "1", "5", "3", "2", "100");
        when(productRepository.findByName("Product2")).thenReturn(Optional.of(product2));

        doNothing().when(quantityService).updateStock(any());

        //WHEN
        var response = availableProductService.sell(items);

        //THEN
        assertNotNull(response);
        assertEquals("700", response.getTotalAmount().toString());
        verify(quantityService, times(1)).updateStock(any());
    }

    @Test
    void sellFailWhenProductNotExist() {
        //GIVEN - product2 not found
        List<PurchaseProduct> items = new ArrayList<>();
        items.add(stubPurchaseProduct("Product1", 5L));
        items.add(stubPurchaseProduct("Product2", 2L));

        var product1 = stubProductEntity(1l, "Product1", "1", "5", "2", "2", "100");
        when(productRepository.findByName("Product1")).thenReturn(Optional.of(product1));

        //WHEN
        var ex = assertThrows(StockNotAvailable.class, () -> availableProductService.sell(items));

        //THEN
        verify(quantityService, never()).updateStock(any());
        assertEquals("Product 'Product2' doesn't exists", ex.getMessage());
    }


}
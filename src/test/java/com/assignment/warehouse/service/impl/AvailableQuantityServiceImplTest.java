package com.assignment.warehouse.service.impl;

import com.assignment.warehouse.db.entity.ArticleEntity;
import com.assignment.warehouse.db.repository.InventoryRepository;
import com.assignment.warehouse.exception.StockNotAvailable;
import com.assignment.warehouse.model.ProductSubArticle;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static com.assignment.warehouse.stubs.TestingData.stubOptArticleEntity;
import static com.assignment.warehouse.stubs.TestingData.stubSubArticle;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AvailableQuantityServiceImplTest {

    @Mock
    private InventoryRepository inventoryRepository;

    private AvailableQuantityServiceImpl availableQuantityService;

    @BeforeEach
    void setUp() {
        availableQuantityService = new AvailableQuantityServiceImpl(inventoryRepository);
    }

    @Test
    void testCalculateSuccess() {
        //GIVEN
        List<ProductSubArticle> parts = new ArrayList<>();
        parts.add(stubSubArticle("1", "10"));
        parts.add(stubSubArticle("2", "4"));

        when(inventoryRepository.findById("1")).thenReturn(stubOptArticleEntity("1", "Screw", "100"));
        when(inventoryRepository.findById("2")).thenReturn(stubOptArticleEntity("2", "leg", "20"));

        //WHEN
        long quantity = availableQuantityService.calculate(parts);

        //THEN
        assertEquals(5, quantity);
        verify(inventoryRepository, times(2)).findById(any());
    }

    @Test
    void testCalculateWhenStockIsLess() {
        //GIVEN
        List<ProductSubArticle> parts = new ArrayList<>();
        parts.add(stubSubArticle("1", "10"));
        parts.add(stubSubArticle("2", "4"));

        when(inventoryRepository.findById("1")).thenReturn(stubOptArticleEntity("1", "Screw", "5"));
        when(inventoryRepository.findById("2")).thenReturn(stubOptArticleEntity("2", "leg", "20"));

        //WHEN
        long quantity = availableQuantityService.calculate(parts);

        //THEN
        assertEquals(0, quantity);
        verify(inventoryRepository, times(2)).findById(any());
    }

    @Test
    void testCalculateWhenNotInStock() {
        //GIVEN, Article 2 is not in stock
        List<ProductSubArticle> parts = new ArrayList<>();
        parts.add(stubSubArticle("1", "10"));
        parts.add(stubSubArticle("2", "4"));

        when(inventoryRepository.findById("1")).thenReturn(stubOptArticleEntity("1", "Screw", "100"));

        //WHEN
        long quantity = availableQuantityService.calculate(parts);

        //THEN
        assertEquals(0, quantity);
        verify(inventoryRepository, times(2)).findById(any());
    }

    @Test
    void updateStockSuccess() {
        //GIVEN
        Map<String, Long> articlesNeeded = new HashMap<>();
        articlesNeeded.put("1", 20L);
        articlesNeeded.put("2", 8L);

        Optional<ArticleEntity> screw = stubOptArticleEntity("1", "Screw", "30");
        Optional<ArticleEntity> leg = stubOptArticleEntity("2", "leg", "10");

        when(inventoryRepository.findById("1")).thenReturn(screw);
        when(inventoryRepository.findById("2")).thenReturn(leg);

        //WHEN
        availableQuantityService.updateStock(articlesNeeded);

        //THEN
        verify(inventoryRepository, times(1)).saveAll(any());
        assertEquals("10", screw.get().getStock());
        assertEquals("2", leg.get().getStock());
    }

    @Test
    void updateStockFailWhenArticleNotFound() {
        //GIVEN - Article 2 not in DB
        Map<String, Long> articlesNeeded = new HashMap<>();
        articlesNeeded.put("1", 20L);
        articlesNeeded.put("2", 8L);

        Optional<ArticleEntity> screw = stubOptArticleEntity("1", "Screw", "30");

        when(inventoryRepository.findById("1")).thenReturn(screw);

        //WHEN
        var ex = assertThrows(StockNotAvailable.class, () -> availableQuantityService.updateStock(articlesNeeded));

        //THEN
        verify(inventoryRepository, never()).saveAll(any());
        assertEquals("Article [2] not found", ex.getMessage());
    }

    @Test
    void updateStockFailWhenStockIsLessThenRequested() {
        //GIVEN
        Map<String, Long> articlesNeeded = new HashMap<>();
        articlesNeeded.put("1", 20L);
        articlesNeeded.put("2", 8L);

        Optional<ArticleEntity> screw = stubOptArticleEntity("1", "Screw", "10");
        Optional<ArticleEntity> leg = stubOptArticleEntity("2", "leg", "10");

        when(inventoryRepository.findById("1")).thenReturn(screw);
        when(inventoryRepository.findById("2")).thenReturn(leg);

        //WHEN
        var ex = assertThrows(StockNotAvailable.class, () -> availableQuantityService.updateStock(articlesNeeded));

        //THEN
        verify(inventoryRepository, never()).saveAll(any());
        assertEquals("Stock not available for article[1] - Screw", ex.getMessage());
    }
}
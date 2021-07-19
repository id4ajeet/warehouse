package com.assignment.warehouse.service.impl;

import com.assignment.warehouse.db.entity.ArticleEntity;
import com.assignment.warehouse.db.mapper.ArticleEntityToArticleConverter;
import com.assignment.warehouse.db.repository.InventoryRepository;
import com.assignment.warehouse.exception.ArticleNotFound;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.assignment.warehouse.stubs.TestingData.stubArticleEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockServiceImplTest {

    @Mock
    private InventoryRepository inventoryRepository;

    private StockServiceImpl stockService;

    @BeforeEach
    void setUp() {
        stockService = new StockServiceImpl(inventoryRepository, new ArticleEntityToArticleConverter());
    }

    @Test
    void findAllSuccess() {
        //GIVEN
        List<ArticleEntity> entities = new ArrayList<>();
        entities.add(stubArticleEntity("1", "Article1", "10"));
        entities.add(stubArticleEntity("2", "Article2", "20"));
        entities.add(stubArticleEntity("3", "Article3", "5"));

        when(inventoryRepository.findAll()).thenReturn(entities);

        //WHEN
        var inventory = stockService.findAll();

        //THEN
        assertNotNull(inventory);
        assertEquals(3, inventory.getArticles().size());
        assertEquals("1", inventory.getArticles().get(0).getId());
        assertEquals("Article1", inventory.getArticles().get(0).getName());
        assertEquals("10", inventory.getArticles().get(0).getStock());
    }

    @Test
    void findAllFailureNothingInDb() {
        //GIVEN
        when(inventoryRepository.findAll()).thenReturn(Collections.emptyList());

        //WHEN
        var ex = assertThrows(ArticleNotFound.class, () -> stockService.findAll());

        //THEN
        assertEquals("Stock is empty", ex.getMessage());
    }

    @Test
    void findOneSuccess() {
        //GIVEN
        var entity = stubArticleEntity("1", "Article1", "10");
        when(inventoryRepository.findById("1")).thenReturn(Optional.of(entity));

        //WHEN
        var article = stockService.findOne("1");

        //THEN
        assertNotNull(article);
        assertEquals("1", article.getId());
        assertEquals("Article1", article.getName());
        assertEquals("10", article.getStock());
    }

    @Test
    void findOneFailure() {
        //GIVEN
        when(inventoryRepository.findById("1")).thenReturn(Optional.empty());

        //WHEN
        var ex = assertThrows(ArticleNotFound.class, () -> stockService.findOne("1"));

        //THEN
        assertEquals("Article[1] not found", ex.getMessage());
    }

    @Test
    void deleteOneSuccess() {
        //GIVEN
        var entity = stubArticleEntity("1", "Article1", "10");
        when(inventoryRepository.findById("1")).thenReturn(Optional.of(entity));

        //WHEN
        stockService.deleteOne("1");

        //THEN
        verify(inventoryRepository, times(1)).delete(entity);
    }

    @Test
    void deleteOneFailure() {
        //GIVEN
        when(inventoryRepository.findById("1")).thenReturn(Optional.empty());

        //WHEN
        stockService.deleteOne("1");

        //THEN
        verify(inventoryRepository, never()).delete(any());
    }
}
package com.assignment.warehouse.service.impl;

import com.assignment.warehouse.db.entity.ProductEntity;
import com.assignment.warehouse.db.entity.ProductSubArticleEntity;
import com.assignment.warehouse.db.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;
import java.util.Optional;

import static com.assignment.warehouse.stubs.TestingData.stubProductEntity;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductFileSystemProcessorImplTest {

    @Mock
    private ProductRepository productRepository;

    private ProductFileSystemProcessorImpl fileSystemProcessor;

    @BeforeEach
    void setUp() {
        fileSystemProcessor = new ProductFileSystemProcessorImpl(new ObjectMapper(), productRepository);
    }

    @Test
    void runSuccess() {
        //GIVEN
        var productsFile = Path.of("src/test/resources/samples/", "products-input.json").toAbsolutePath().normalize().toString();
        var entity = stubProductEntity(1l, "Dining Chair", "1", "5", "5", "2", "100");
        when(productRepository.findByName("Dining Chair")).thenReturn(Optional.of(entity));

        //WHEN
        fileSystemProcessor.run(productsFile);

        //THEN
        verify(productRepository, times(1)).saveAll(any());

        assertEquals("Dining Chair", entity.getName());
        assertEquals("100.50", entity.getPrice());
        assertEquals(1l, entity.getId());
        assertEquals(4, entity.getSubArticles().size());

        //check Update
        var entity1 = find(entity, "1");
        assertTrue(entity1.isPresent());
        assertEquals("4", entity1.get().getArticleQuantity());

        //Check Remove
        var entity2 = find(entity, "5");
        assertFalse(entity2.isPresent());

        //Check Create
        var entity3 = find(entity, "2");
        assertTrue(entity3.isPresent());
        assertEquals("8", entity3.get().getArticleQuantity());
    }

    private Optional<ProductSubArticleEntity> find(ProductEntity entity, String id) {
        return entity.getSubArticles().stream().filter(e -> e.getArticleId().equals(id)).findFirst();
    }

    @ParameterizedTest
    @ValueSource(strings = {"empty.json", "products-not-exists.json"})
    void runFailure(String fileName) {
        //GIVEN
        var inventoryFile = Path.of("src/test/resources/samples/", fileName).toAbsolutePath().normalize().toString();

        //WHEN
        fileSystemProcessor.run(inventoryFile);

        //THEN
        verify(productRepository, never()).saveAll(any());
    }
}
package com.assignment.warehouse.service.impl;

import com.assignment.warehouse.db.repository.InventoryRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.nio.file.Path;

import static com.assignment.warehouse.stubs.TestingData.stubOptArticleEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class InventoryFileSystemProcessorImplTest {

    @Mock
    private InventoryRepository inventoryRepository;

    private InventoryFileSystemProcessorImpl fileSystemProcessor;

    @BeforeEach
    void setUp() {
        fileSystemProcessor = new InventoryFileSystemProcessorImpl(new ObjectMapper(), inventoryRepository);
    }

    @Test
    void runSuccess() {
        //GIVEN
        var inventoryFile = Path.of("src/test/resources/samples/", "inventory-input.json").toAbsolutePath().normalize().toString();
        var article1 = stubOptArticleEntity("1", "Article1", "10");
        when(inventoryRepository.findById("1")).thenReturn(article1);

        //WHEN
        fileSystemProcessor.run(inventoryFile);

        //THEN
        verify(inventoryRepository, times(1)).saveAll(any());
        assertEquals("leg", article1.get().getName());
        assertEquals("120", article1.get().getStock());
    }

    @ParameterizedTest
    @ValueSource(strings = {"empty.json", "inventory-not-exists.json"})
    void runFailure(String fileName) {
        //GIVEN
        var inventoryFile = Path.of("src/test/resources/samples/", fileName).toAbsolutePath().normalize().toString();

        //WHEN
        fileSystemProcessor.run(inventoryFile);

        //THEN
        verify(inventoryRepository, never()).saveAll(any());
    }
}
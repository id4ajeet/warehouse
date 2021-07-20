package com.assignment.warehouse.config;

import com.assignment.warehouse.db.repository.InventoryRepository;
import com.assignment.warehouse.db.repository.ProductRepository;
import com.assignment.warehouse.service.InputDataProcessor;
import com.assignment.warehouse.service.impl.FileSystemChangeListener;
import com.assignment.warehouse.service.impl.InventoryFileSystemProcessorImpl;
import com.assignment.warehouse.service.impl.ProductFileSystemProcessorImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.devtools.filewatch.FileChangeListener;
import org.springframework.boot.devtools.filewatch.FileSystemWatcher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;
import java.time.Duration;

@Configuration
@Slf4j
public class FileWatcherConfig {

    @Bean
    public InputDataProcessor inventoryInputProcessor(final ObjectMapper objectMapper,
                                                      final InventoryRepository inventoryRepository) {
        return new InventoryFileSystemProcessorImpl(objectMapper, inventoryRepository);
    }

    @Bean
    public InputDataProcessor productInputProcessor(final ObjectMapper objectMapper,
                                                    final ProductRepository productRepository) {
        return new ProductFileSystemProcessorImpl(objectMapper, productRepository);
    }


    @Bean
    public FileChangeListener fileChangeListener(@Value("${warehouse.fs.listener.inventoryFile.prefix:inventory}") final String inventoryFilePrefix,
                                                 @Value("${warehouse.fs.listener.productFile.prefix:product}") final String productFilePrefix,
                                                 final InputDataProcessor inventoryInputProcessor,
                                                 final InputDataProcessor productInputProcessor) {
        return new FileSystemChangeListener(inventoryFilePrefix, productFilePrefix, inventoryInputProcessor, productInputProcessor);
    }

    @Bean(destroyMethod = "stop")
    public FileSystemWatcher fileSystemWatcher(@Value("${warehouse.fs.listener.directory.name}") final String inventoryDirectory,
                                               @Value("${warehouse.fs.listener.directory.pollInterval:4000}") final long pollInterval,
                                               @Value("${warehouse.fs.listener.directory.quietPeriod:3000}") final long quietPeriod,
                                               final FileChangeListener fileChangeListener) {

        File directory = new File(inventoryDirectory);
        if (!directory.exists()) {
            log.info("{} directory doesn't exists, creating...", inventoryDirectory);
            directory.mkdirs();
        }

        var watcher = new FileSystemWatcher(true, Duration.ofMillis(pollInterval), Duration.ofMillis(quietPeriod));
        watcher.addSourceDirectory(directory);
        watcher.addListener(fileChangeListener);
        watcher.start();
        log.info("started watching directory - {}", inventoryDirectory);
        return watcher;
    }
}

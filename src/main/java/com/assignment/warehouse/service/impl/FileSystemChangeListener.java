package com.assignment.warehouse.service.impl;

import com.assignment.warehouse.service.InputDataProcessor;
import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.devtools.filewatch.ChangedFiles;
import org.springframework.boot.devtools.filewatch.FileChangeListener;

import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.Set;

import static org.springframework.boot.devtools.filewatch.ChangedFile.Type.DELETE;

@Slf4j
@AllArgsConstructor
public class FileSystemChangeListener implements FileChangeListener {

    @NonNull
    private final String inventoryFilePrefix;

    @NonNull
    private final String productFilePrefix;

    @NonNull
    private final InputDataProcessor inventoryInputProcessor;

    @NonNull
    private final InputDataProcessor productInputProcessor;

    @Override
    public void onChange(final Set<ChangedFiles> changeSet) {
        for (var change : changeSet) {
            for (var file : change) {
                final var filePath = file.getFile()
                        .toPath()
                        .toAbsolutePath()
                        .normalize()
                        .toString();

                final var action = file.getType();

                if (DELETE.equals(action) || !file.getFile().exists() || file.getFile().isHidden()) {
                    log.warn("Skipping file - {} , Action - {}", filePath, action);
                    continue;
                }

                if (!isLocked(filePath)) {
                    log.info("Processing file - {}, Action - {}", filePath, action);
                    process(filePath);
                }
            }
        }
    }

    private void process(final String filePath) {

        final var fileName = Path.of(filePath)
                .getFileName()
                .toString()
                .toLowerCase();

        if (fileName.startsWith(inventoryFilePrefix.toLowerCase())) {
            inventoryInputProcessor.run(filePath);
        } else if (fileName.startsWith(productFilePrefix.toLowerCase())) {
            productInputProcessor.run(filePath);
        } else {
            log.warn("Skipping, No processor found for file {}", filePath);
        }
    }

    private boolean isLocked(final String filePath) {
        try (var ch = FileChannel.open(Path.of(filePath), StandardOpenOption.WRITE); var lock = ch.tryLock()) {
            return Objects.isNull(lock);
        } catch (Exception e) {
            log.error("File {} is locked", filePath, e);
            return true;
        }
    }
}

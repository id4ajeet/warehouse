package com.assignment.warehouse.service.impl;

import com.assignment.warehouse.service.InputDataProcessor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.devtools.filewatch.ChangedFile;
import org.springframework.boot.devtools.filewatch.ChangedFiles;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.HashSet;
import java.util.Set;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FileSystemChangeListenerTest {

    @Mock
    private InputDataProcessor inventoryInputProcessor;

    @Mock
    private InputDataProcessor productInputProcessor;

    private FileSystemChangeListener changeListener;

    @BeforeEach
    void setUp() {
        changeListener = new FileSystemChangeListener("inventory", "product", inventoryInputProcessor, productInputProcessor);
    }

    @Test
    void onChangeSuccess() {
        //GIVEN
        var sourceDirectory = Path.of("src/test/resources/samples/").toFile();
        var inventoryFile = Path.of(sourceDirectory.toString(), "inventory-input.json").toFile();
        var productFile = Path.of(sourceDirectory.toString(), "products-input.json").toFile();

        Set<ChangedFile> files1 = new HashSet<>();
        files1.add(new ChangedFile(sourceDirectory, inventoryFile, ChangedFile.Type.ADD));

        Set<ChangedFile> files2 = new HashSet<>();
        files2.add(new ChangedFile(sourceDirectory, productFile, ChangedFile.Type.MODIFY));

        Set<ChangedFiles> changeSet = new HashSet<>();
        changeSet.add(new ChangedFiles(sourceDirectory, files1));
        changeSet.add(new ChangedFiles(sourceDirectory, files2));

        //WHEN
        changeListener.onChange(changeSet);

        //THEN
        verify(inventoryInputProcessor, times(1)).run(contains("inventory-input.json"));
        verify(productInputProcessor, times(1)).run(contains("products-input.json"));
    }

    @Test
    void onChangeNoProcessingOnDelete() {
        //GIVEN
        var sourceDirectory = Path.of("src/test/resources/samples/").toFile();
        var inventoryFile = Path.of(sourceDirectory.toString(), "inventory-input.json").toFile();
        var productFile = Path.of(sourceDirectory.toString(), "products-input.json").toFile();

        Set<ChangedFile> files1 = new HashSet<>();
        files1.add(new ChangedFile(sourceDirectory, inventoryFile, ChangedFile.Type.DELETE));

        Set<ChangedFile> files2 = new HashSet<>();
        files2.add(new ChangedFile(sourceDirectory, productFile, ChangedFile.Type.DELETE));

        Set<ChangedFiles> changeSet = new HashSet<>();
        changeSet.add(new ChangedFiles(sourceDirectory, files1));
        changeSet.add(new ChangedFiles(sourceDirectory, files2));

        //WHEN
        changeListener.onChange(changeSet);

        //THEN
        verify(inventoryInputProcessor, never()).run(any());
        verify(productInputProcessor, never()).run(any());
    }

    @ParameterizedTest
    @ValueSource(strings = {"dummy-no-processor.json", ".HiddenFile", "inventory-not-exists.json"})
    void onChangeNoProcessing(String filename) {
        //GVEN
        var sourceDirectory = Path.of("src/test/resources/samples/").toFile();
        var inventoryFile = Path.of(sourceDirectory.toString(), filename).toFile();

        Set<ChangedFile> files1 = new HashSet<>();
        files1.add(new ChangedFile(sourceDirectory, inventoryFile, ChangedFile.Type.ADD));

        Set<ChangedFiles> changeSet = new HashSet<>();
        changeSet.add(new ChangedFiles(sourceDirectory, files1));

        //WHEN
        changeListener.onChange(changeSet);

        //THEN
        verify(inventoryInputProcessor, never()).run(any());
        verify(productInputProcessor, never()).run(any());
    }

    @Test
    void onChangeNoProcessingIfFileIsLocked() throws IOException {
        //GIVEN
        var sourceDirectory = Path.of("src/test/resources/samples/").toFile();
        var inventoryFile = Path.of(sourceDirectory.toString(), "lockedFile.json").toFile();

        Set<ChangedFile> files1 = new HashSet<>();
        files1.add(new ChangedFile(sourceDirectory, inventoryFile, ChangedFile.Type.ADD));

        Set<ChangedFiles> changeSet = new HashSet<>();
        changeSet.add(new ChangedFiles(sourceDirectory, files1));

        var ch = FileChannel.open(inventoryFile.toPath(), StandardOpenOption.WRITE);
        FileLock lock = ch.tryLock();

        //WHEN
        changeListener.onChange(changeSet);

        //THEN
        verify(inventoryInputProcessor, never()).run(any());
        verify(productInputProcessor, never()).run(any());

        lock.release();
    }

}
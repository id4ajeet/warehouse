package com.assignment.warehouse;

import ch.qos.logback.classic.Logger;
import com.assignment.warehouse.stubs.TestLogAppender;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@ActiveProfiles("test")
class WarehouseApplicationTests {

    private TestLogAppender logAppender = new TestLogAppender();
    private Logger logs = (Logger) LoggerFactory.getLogger("org.springframework");

    @BeforeEach
    void setUp() {
        var inventory = Path.of("target/inventory").toFile();
        if (inventory.exists()) {
            inventory.delete();
        }

        logAppender.clear();
        logs.addAppender(logAppender);
    }

    @Test
    void startupTest() {
        try {
            //GIVEN
            System.setProperty("spring.profiles.active", "test");

            //WHEN
            WarehouseApplication.main(new String[]{});

            //THEN
            assertTrue(logAppender.logs().contains("[INFO] Tomcat started on port(s)"));
        } catch (Exception e) {
            assertTrue(logAppender.logs().contains(" was already in use."));
        }
    }
}

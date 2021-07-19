package com.assignment.warehouse.controller;

import com.assignment.warehouse.service.InputDataProcessor;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.file.Path;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class StockControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private InputDataProcessor inventoryInputProcessor;

    @BeforeEach
    void setUp() {
        var inventoryFile = Path.of("src/test/resources/samples/", "inventory-input.json").toAbsolutePath().normalize().toString();
        inventoryInputProcessor.run(inventoryFile);
    }

    @Test
    void testGetAllArticles() throws Exception {
        mvc.perform(get("/v1/stock/articles"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(Matchers.containsString("\"name\":\"seat\"")));
    }

    @Test
    void testGetArticleSuccess() throws Exception {
        mvc.perform(get("/v1/stock/articles/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(Matchers.containsString("\"name\":\"leg\"")));
    }

    @Test
    void testGetArticleNotFound() throws Exception {
        mvc.perform(get("/v1/stock/articles/20"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(Matchers.containsString("{\"message\":\"Article[20] not found\"}")));
    }

    @Test
    void testDeleteArticle() throws Exception {
        mvc.perform(delete("/v1/stock/articles/1"))
                .andExpect(status().isOk());
    }
}
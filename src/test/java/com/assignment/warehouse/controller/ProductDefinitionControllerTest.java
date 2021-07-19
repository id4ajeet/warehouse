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
class ProductDefinitionControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private InputDataProcessor productInputProcessor;

    @BeforeEach
    void setUp() {
        var productsFile = Path.of("src/test/resources/samples/", "products-input.json").toAbsolutePath().normalize().toString();
        productInputProcessor.run(productsFile);
    }

    @Test
    void testGetAllProducts() throws Exception {
        mvc.perform(get("/v1/definition/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(Matchers.containsString("\"name\":\"Dining Chair\"")));
    }

    @Test
    void testGetProductSuccess() throws Exception {
        mvc.perform(get("/v1/definition/products/Dining Chair"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(Matchers.containsString("\"name\":\"Dining Chair\"")));
    }

    @Test
    void testGetProductNotFound() throws Exception {
        mvc.perform(get("/v1/definition/products/NotExist"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(Matchers.containsString("{\"message\":\"Product[NotExist] not found\"}")));
    }

    @Test
    void testDeleteProduct() throws Exception {
        mvc.perform(delete("/v1/definition/products/Dining Chair"))
                .andExpect(status().isOk());
    }
}
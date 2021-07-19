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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class PurchaseControllerTest {

    @Autowired
    private MockMvc mvc;

    @Autowired
    private InputDataProcessor productInputProcessor;

    @Autowired
    private InputDataProcessor inventoryInputProcessor;

    @BeforeEach
    void setUp() {
        var inventoryFile = Path.of("src/test/resources/samples/", "inventory-input.json").toAbsolutePath().normalize().toString();
        inventoryInputProcessor.run(inventoryFile);

        var productsFile = Path.of("src/test/resources/samples/", "products-input.json").toAbsolutePath().normalize().toString();
        productInputProcessor.run(productsFile);
    }

    @Test
    void testGetAllProducts() throws Exception {
        mvc.perform(get("/v1/purchase/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(Matchers.containsString("\"name\":\"Dining Chair\"")));
    }

    @Test
    void testUpdateProductsSuccess() throws Exception {
        mvc.perform(post("/v1/purchase/products")
                .content("[\n" +
                        "    {\n" +
                        "        \"name\": \"Table Fan\",\n" +
                        "        \"quantity\": \"2\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"name\": \"Door\",\n" +
                        "        \"quantity\": \"3\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"name\": \"Dinning Table\",\n" +
                        "        \"quantity\": \"1\"\n" +
                        "    }\n" +
                        "]")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(Matchers.containsString("{\"totalAmount\":\"1050.75\"}")));
    }

    @Test
    void testUpdateProductsBadRequest() throws Exception {
        mvc.perform(post("/v1/purchase/products")
                .content("[\n" +
                        "    {\n" +
                        "        \"name\": \"Table Fan\",\n" +
                        "        \"quantity\": \"2\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"name\": \"Door\",\n" +
                        "        \"quantity\": \"5\"\n" +
                        "    },\n" +
                        "    {\n" +
                        "        \"name\": \"Dinning Table\",\n" +
                        "        \"quantity\": \"1\"\n" +
                        "    }\n" +
                        "]")
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().string(Matchers.containsString("{\"message\":\"Stock not available for article[8] - Door lock\"}")));
    }
}
package com.assignment.warehouse.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class InventoryTest {

    private final static String INVENTORY = "{\n" +
            "  \"inventory\": [\n" +
            "    {\n" +
            "      \"art_id\": \"1\",\n" +
            "      \"name\": \"leg\",\n" +
            "      \"stock\": \"12\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"art_id\": \"2\",\n" +
            "      \"name\": \"screw\",\n" +
            "      \"stock\": \"17\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"art_id\": \"3\",\n" +
            "      \"name\": \"seat\",\n" +
            "      \"stock\": \"2\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"art_id\": \"4\",\n" +
            "      \"name\": \"table top\",\n" +
            "      \"stock\": \"1\"\n" +
            "    }\n" +
            "  ]\n" +
            "}\n";

    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void convertToPojo() throws JsonProcessingException {
        //WHEN
        Inventory inventory = objectMapper.readValue(INVENTORY, Inventory.class);
        List<Article> articles = inventory.getArticles();

        //THEN
        assertNotNull(articles);
        assertEquals(4, articles.size());
        assertEquals("1", articles.get(0).getId());
        assertEquals("leg", articles.get(0).getName());
        assertEquals("12", articles.get(0).getStock());
    }

    @Test
    void convertToJson() throws JsonProcessingException {
        //GIVEN
        Article article1 = new Article();
        article1.setId("1");
        article1.setName("leg");
        article1.setStock("12");

        Article article2 = new Article();
        article2.setId("2");
        article2.setName("screw");
        article2.setStock("17");

        Article article3 = new Article();
        article3.setId("3");
        article3.setName("seat");
        article3.setStock("2");

        Article article4 = new Article();
        article4.setId("4");
        article4.setName("table top");
        article4.setStock("1");

        List<Article> articles = new ArrayList<>();
        articles.add(article1);
        articles.add(article2);
        articles.add(article3);
        articles.add(article4);

        Inventory inventory = new Inventory();
        inventory.setArticles(articles);

        //WHEN
        var json = objectMapper.writeValueAsString(inventory);

        //THEN
        var actual = objectMapper.readTree(json);
        var expected = objectMapper.readTree(INVENTORY);
        assertEquals(expected, actual);
    }
}
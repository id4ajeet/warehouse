package com.assignment.warehouse.model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductsTest {

    private final static String PRODUCTS = "{\n" +
            "  \"products\": [\n" +
            "    {\n" +
            "      \"name\": \"Dining Chair\",\n" +
            "      \"contain_articles\": [\n" +
            "        {\n" +
            "          \"art_id\": \"1\",\n" +
            "          \"amount_of\": \"4\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"art_id\": \"2\",\n" +
            "          \"amount_of\": \"8\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"art_id\": \"3\",\n" +
            "          \"amount_of\": \"1\"\n" +
            "        }\n" +
            "      ]\n" +
            "    },\n" +
            "    {\n" +
            "      \"name\": \"Dining Table\",\n" +
            "      \"contain_articles\": [\n" +
            "        {\n" +
            "          \"art_id\": \"1\",\n" +
            "          \"amount_of\": \"4\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"art_id\": \"2\",\n" +
            "          \"amount_of\": \"8\"\n" +
            "        },\n" +
            "        {\n" +
            "          \"art_id\": \"4\",\n" +
            "          \"amount_of\": \"1\"\n" +
            "        }\n" +
            "      ]\n" +
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
        //GIVEN & WHEN
        var actual = objectMapper.readValue(PRODUCTS, Products.class);

        //THEN
        assertNotNull(actual);
        assertEquals(2, actual.getItems().size());

        var product1 = actual.getItems().get(0);
        assertEquals("Dining Chair", product1.getName());
        assertNull(product1.getPrice());
        assertEquals(3, product1.getParts().size());

        var article1 = product1.getParts().get(0);
        assertEquals("1", article1.getArticleId());
        assertEquals("4", article1.getArticleCount());
    }

    @Test
    void convertToJson() throws JsonProcessingException {
        //GIVEN
        Product product1 = new Product();
        product1.setName("Dining Chair");

        List<ProductSubArticle> parts1 = new ArrayList<>();

        var article11 = new ProductSubArticle();
        article11.setArticleId("1");
        article11.setArticleCount("4");
        parts1.add(article11);

        var article12 = new ProductSubArticle();
        article12.setArticleId("2");
        article12.setArticleCount("8");
        parts1.add(article12);

        var article13 = new ProductSubArticle();
        article13.setArticleId("3");
        article13.setArticleCount("1");
        parts1.add(article13);

        product1.setParts(parts1);

        var product2 = new Product();
        product2.setName("Dining Table");

        List<ProductSubArticle> parts2 = new ArrayList<>();

        var article21 = new ProductSubArticle();
        article21.setArticleId("1");
        article21.setArticleCount("4");
        parts2.add(article21);

        var article22 = new ProductSubArticle();
        article22.setArticleId("2");
        article22.setArticleCount("8");
        parts2.add(article22);

        var article23 = new ProductSubArticle();
        article23.setArticleId("4");
        article23.setArticleCount("1");
        parts2.add(article23);

        product2.setParts(parts2);

        List<Product> allProducts = new ArrayList<>();
        allProducts.add(product1);
        allProducts.add(product2);

        var products = new Products();
        products.setItems(allProducts);

        //WHEN
        var json = objectMapper.writeValueAsString(products);

        //THEN
        var actual = objectMapper.readTree(json);
        var expected = objectMapper.readTree(PRODUCTS);
        assertEquals(expected, actual);
    }
}
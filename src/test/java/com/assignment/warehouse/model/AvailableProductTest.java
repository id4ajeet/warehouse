package com.assignment.warehouse.model;

import org.junit.jupiter.api.Test;

import static com.assignment.warehouse.stubs.TestingData.stubAvailableProduct;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class AvailableProductTest {

    @Test
    void testEqualsAndHashCode() {
        //GIVEN product 1 & 2 are equals but 3 is not
        var product1 = stubAvailableProduct("Fan", "100", 10L, "1", "4", "2", "2");
        var product2 = stubAvailableProduct("Fan", "100", 10L, "1", "4", "2", "2");
        var product3 = stubAvailableProduct("TV", "100", 10L, "1", "4", "2", "2");

        //THEN
        assertEquals(product1, product2);
        assertEquals(product1.hashCode(), product2.hashCode());
        assertNotEquals(product1, product3);
    }
}
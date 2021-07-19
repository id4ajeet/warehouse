package com.assignment.warehouse.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Products {

    @JsonProperty("products")
    private List<Product> items;
}

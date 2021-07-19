package com.assignment.warehouse.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
@JsonInclude(Include.NON_NULL)
public class Product {
    private String name;
    private String price;
    @JsonProperty("contain_articles")
    private List<ProductSubArticle> parts;
}

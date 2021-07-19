package com.assignment.warehouse.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Article {
    @JsonProperty("art_id")
    private String id;
    private String name;
    private String stock;
}

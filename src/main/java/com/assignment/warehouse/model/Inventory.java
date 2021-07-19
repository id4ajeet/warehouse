package com.assignment.warehouse.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class Inventory {
    @JsonProperty("inventory")
    private List<Article> articles;
}

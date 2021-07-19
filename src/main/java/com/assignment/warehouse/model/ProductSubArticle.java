package com.assignment.warehouse.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ProductSubArticle {
    @JsonProperty("art_id")
    private String articleId;
    @JsonProperty("amount_of")
    private String articleCount;
}

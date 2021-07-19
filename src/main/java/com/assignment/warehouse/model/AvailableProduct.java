package com.assignment.warehouse.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AvailableProduct extends Product{
    private long quantity;
}

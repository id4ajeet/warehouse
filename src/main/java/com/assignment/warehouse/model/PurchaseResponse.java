package com.assignment.warehouse.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Data
public class PurchaseResponse {
    @JsonFormat(shape = STRING)
    private BigDecimal totalAmount;
}

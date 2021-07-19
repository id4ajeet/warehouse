package com.assignment.warehouse.exception;

public class StockNotAvailable extends RuntimeException {
    public StockNotAvailable(String message) {
        super(message);
    }
}

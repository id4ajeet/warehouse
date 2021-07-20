package com.assignment.warehouse.exception;

public class StockNotAvailable extends RuntimeException {
    public StockNotAvailable(final String message) {
        super(message);
    }
}

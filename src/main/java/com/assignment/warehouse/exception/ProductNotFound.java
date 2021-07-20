package com.assignment.warehouse.exception;

public class ProductNotFound extends RuntimeException{
    public ProductNotFound(final String message) {
        super(message);
    }
}

package com.assignment.warehouse.exception;

public class ArticleNotFound extends RuntimeException {
    public ArticleNotFound(String message) {
        super(message);
    }
}

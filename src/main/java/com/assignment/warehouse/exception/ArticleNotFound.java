package com.assignment.warehouse.exception;

public class ArticleNotFound extends RuntimeException {
    public ArticleNotFound(final String message) {
        super(message);
    }
}

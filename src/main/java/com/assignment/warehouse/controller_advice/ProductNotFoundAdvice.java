package com.assignment.warehouse.controller_advice;

import com.assignment.warehouse.exception.ProductNotFound;
import com.assignment.warehouse.model.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ProductNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(ProductNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage articleNotFoundHandler(ProductNotFound ex) {
        ErrorMessage error = new ErrorMessage();
        error.setMessage(ex.getMessage());
        return error;
    }
}

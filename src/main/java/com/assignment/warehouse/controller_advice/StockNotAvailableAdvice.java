package com.assignment.warehouse.controller_advice;

import com.assignment.warehouse.exception.StockNotAvailable;
import com.assignment.warehouse.model.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class StockNotAvailableAdvice {
    @ResponseBody
    @ExceptionHandler(StockNotAvailable.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorMessage articleNotFoundHandler(final StockNotAvailable ex) {
        var error = new ErrorMessage();
        error.setMessage(ex.getMessage());
        return error;
    }
}

package com.assignment.warehouse.controller_advice;

import com.assignment.warehouse.exception.ArticleNotFound;
import com.assignment.warehouse.model.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ArticleNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(ArticleNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorMessage articleNotFoundHandler(final ArticleNotFound ex) {
        var error = new ErrorMessage();
        error.setMessage(ex.getMessage());
        return error;
    }
}

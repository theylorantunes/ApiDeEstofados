package com.estofados.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class EstofadoNotFoundAdvice {

    @ResponseBody
    @ExceptionHandler(EstofadoNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String estofadoNotFoundHandler(EstofadoNotFoundException ex) {
        return ex.getMessage();
    }
}
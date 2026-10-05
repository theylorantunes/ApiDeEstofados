package com.estofados.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
@ControllerAdvice
public class FabricanteNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(FabricanteNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String handler(FabricanteNotFoundException ex) { return ex.getMessage(); }
}
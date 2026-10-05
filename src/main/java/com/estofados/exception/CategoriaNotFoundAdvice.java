package com.estofados.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
@ControllerAdvice
public class CategoriaNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(CategoriaNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String categoriaNotFoundHandler(CategoriaNotFoundException ex) { return ex.getMessage(); }
}
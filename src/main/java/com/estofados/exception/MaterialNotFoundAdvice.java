package com.estofados.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
@ControllerAdvice
public class MaterialNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(MaterialNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String handler(MaterialNotFoundException ex) { return ex.getMessage(); }
}


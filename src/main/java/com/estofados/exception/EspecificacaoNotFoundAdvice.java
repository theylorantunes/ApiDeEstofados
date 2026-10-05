package com.estofados.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@ControllerAdvice
public class EspecificacaoNotFoundAdvice {
    @ResponseBody
    @ExceptionHandler(EspecificacaoNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    String handler(EspecificacaoNotFoundException ex) { return ex.getMessage(); }
}
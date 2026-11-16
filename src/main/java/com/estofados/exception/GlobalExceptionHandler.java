package com.estofados.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            CategoriaNotFoundException.class,
            EspecificacaoNotFoundException.class,
            EstofadoNotFoundException.class,
            FabricanteNotFoundException.class,
            MaterialNotFoundException.class
    })
    public ResponseEntity<ErroPadrao> handleNotFound(RuntimeException ex, HttpServletRequest request) {
        ErroPadrao erro = new ErroPadrao(
                LocalDateTime.now(),
                HttpStatus.NOT_FOUND.value(),
                "Não Encontrado",
                "O item que você tentou acessar não existe no nosso sistema.",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroPadrao> handleValidationErrors(MethodArgumentNotValidException ex, HttpServletRequest request) {
        // Pega todos os campos que deram erro e junta numa frase fácil de ler
        String camposInvalidos = ex.getBindingResult().getFieldErrors().stream()
                .map(erroDeCampo -> erroDeCampo.getField() + ": " + erroDeCampo.getDefaultMessage())
                .collect(Collectors.joining(" | "));

        ErroPadrao erro = new ErroPadrao(
                LocalDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                "Dados Incorretos",
                "Não foi possível salvar. Verifique os dados enviados: " + camposInvalidos,
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroPadrao> handleGenericError(Exception ex, HttpServletRequest request) {
        ErroPadrao erro = new ErroPadrao(
                LocalDateTime.now(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro Interno",
                "Ocorreu um problema inesperado no nosso servidor. Por favor, tente novamente mais tarde.",
                request.getRequestURI()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }
}
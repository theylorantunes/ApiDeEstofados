package com.estofados.exception;

public class EstofadoNotFoundException extends RuntimeException {
    public EstofadoNotFoundException(Long id) {
        super("Não foi possível encontrar o estofado com o id: " + id);
    }
}
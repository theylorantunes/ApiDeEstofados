package com.estofados.exception;

public class EspecificacaoNotFoundException extends RuntimeException {
    public EspecificacaoNotFoundException(Long id) { super("Especificação não encontrada com o id: " + id); }
}
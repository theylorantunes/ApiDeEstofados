package com.estofados.exception;
public class CategoriaNotFoundException extends RuntimeException {
    public CategoriaNotFoundException(Long id) { super("Categoria não encontrada com o id: " + id); }
}
package com.estofados.exception;
public class MaterialNotFoundException extends RuntimeException {
    public MaterialNotFoundException(Long id) { super("Material não encontrado com o id: " + id); }
}
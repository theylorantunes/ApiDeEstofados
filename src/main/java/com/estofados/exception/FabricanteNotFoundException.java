package com.estofados.exception;
public class FabricanteNotFoundException extends RuntimeException {
    public FabricanteNotFoundException(Long id) { super("Fabricante não encontrado com o id: " + id); }
}
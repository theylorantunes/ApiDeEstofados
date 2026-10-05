package com.estofados.controller.dto;
import jakarta.validation.constraints.NotBlank;

public record CategoriaRecordDto(@NotBlank String nomeCategoria, @NotBlank String descricaoUso) {}
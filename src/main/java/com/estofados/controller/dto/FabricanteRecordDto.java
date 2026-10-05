package com.estofados.controller.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record FabricanteRecordDto(@NotBlank String nomeFabricante, @NotBlank String cnpj, @NotBlank String cidadeSede, @NotNull Long categoriaId) {}
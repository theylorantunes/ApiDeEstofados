package com.estofados.controller.v2;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record Detalhes(@NotNull BigDecimal preco, @NotBlank String corPrincipal) {}
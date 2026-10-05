package com.estofados.controller.dto;
import com.estofados.enums.StatusFabricacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

public record EstofadoRecordDto(
        @NotBlank String nomeModelo,
        @NotNull BigDecimal preco,
        @NotBlank String corPrincipal,
        @NotNull StatusFabricacao status,
        @NotNull Long fabricanteId,
        @NotNull Long especificacaoId,
        @NotNull List<Long> materiaisIds
) {}
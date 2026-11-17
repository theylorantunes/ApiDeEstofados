package com.estofados.controller.v2;
import com.estofados.enums.StatusFabricacao;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record EstofadoRequest(
        @NotBlank String nomeModelo,
        @NotNull @Valid Detalhes detalhes,
        @NotNull StatusFabricacao status,
        @NotNull Long fabricanteId,
        @NotNull Long especificacaoId,
        @NotNull List<Long> materiaisIds) {}
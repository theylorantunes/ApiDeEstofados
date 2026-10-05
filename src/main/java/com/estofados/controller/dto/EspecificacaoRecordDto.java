package com.estofados.controller.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EspecificacaoRecordDto(@NotBlank String dimensoes, @NotNull Integer pesoSuportadoKg, @NotBlank String densidadeEspuma) {}
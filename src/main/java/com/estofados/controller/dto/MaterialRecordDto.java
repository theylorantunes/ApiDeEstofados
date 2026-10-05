package com.estofados.controller.dto;
import jakarta.validation.constraints.NotBlank;

public record MaterialRecordDto(@NotBlank String nomeMaterial, @NotBlank String tipoParte) {}
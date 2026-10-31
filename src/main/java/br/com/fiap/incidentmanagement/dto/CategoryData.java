package br.com.fiap.incidentmanagement.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryData(@NotBlank String name) {
}

package br.com.fiap.incidentmanagement.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginData(
        @NotBlank String login,
        @NotBlank String password) {
}

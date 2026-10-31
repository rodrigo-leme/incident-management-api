package br.com.fiap.incidentmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRoleData(
        @NotNull Long id,
        @NotBlank String role) {
}

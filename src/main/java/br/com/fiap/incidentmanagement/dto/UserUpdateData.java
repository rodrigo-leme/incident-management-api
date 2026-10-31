package br.com.fiap.incidentmanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserUpdateData(
        @NotNull Long id,
        @NotBlank String name,
        @NotBlank @Email String login) {
}

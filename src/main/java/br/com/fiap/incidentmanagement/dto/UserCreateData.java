package br.com.fiap.incidentmanagement.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UserCreateData(
        @NotBlank String name,
        @NotBlank @Email String login,
        @NotBlank @Size(min = 6) String password,
        @NotBlank String role) {
}

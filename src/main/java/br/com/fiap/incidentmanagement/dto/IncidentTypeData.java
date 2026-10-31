package br.com.fiap.incidentmanagement.dto;

import jakarta.validation.constraints.NotBlank;

public record IncidentTypeData(
        @NotBlank String name,
        @NotBlank String category) {
}

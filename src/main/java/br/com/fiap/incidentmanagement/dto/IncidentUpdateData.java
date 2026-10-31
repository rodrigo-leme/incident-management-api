package br.com.fiap.incidentmanagement.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record IncidentUpdateData(
        @NotNull Long id,
        @NotNull Long idSchoolClass,
        @NotNull Long idStudent,
        @NotBlank String category,
        @NotBlank String type,
        @NotBlank @Size(max = 400) String description) {
}

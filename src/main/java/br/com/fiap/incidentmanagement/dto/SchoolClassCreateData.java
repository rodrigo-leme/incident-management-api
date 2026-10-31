package br.com.fiap.incidentmanagement.dto;

import br.com.fiap.incidentmanagement.entity.Semester;
import br.com.fiap.incidentmanagement.entity.Shift;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SchoolClassCreateData(
        @NotBlank String name,
        @NotNull Shift shift,
        @NotNull Integer year,
        @NotNull Semester semester) {
}

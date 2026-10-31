package br.com.fiap.incidentmanagement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public record IncidentCreateData(
        @NotNull Long idSchoolClass,
        @NotNull Long idStudent,
        @NotNull @JsonFormat(pattern = "dd/MM/yyyy - HH:mm") LocalDateTime registerDate,
        @NotBlank String category,
        @NotBlank String type,
        @NotBlank @Size(max = 400) String description) {
}

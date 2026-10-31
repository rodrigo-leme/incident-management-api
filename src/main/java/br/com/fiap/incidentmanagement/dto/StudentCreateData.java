package br.com.fiap.incidentmanagement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record StudentCreateData(
        @NotBlank String name,
        @NotNull @JsonFormat(pattern = "dd/MM/yyyy") LocalDate birthDate) {
}

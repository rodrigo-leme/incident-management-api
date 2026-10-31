package br.com.fiap.incidentmanagement.dto;

import br.com.fiap.incidentmanagement.entity.Status;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record IncidentStatusData(
        @NotNull Long id,
        @NotNull Status status,
        @NotNull @JsonFormat(pattern = "dd/MM/yyyy - HH:mm") LocalDateTime updateDate) {
}

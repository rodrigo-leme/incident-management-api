package br.com.fiap.incidentmanagement.dto;

import br.com.fiap.incidentmanagement.entity.Incident;
import br.com.fiap.incidentmanagement.entity.Status;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record IncidentResponse(
        Long id,
        Long idSchoolClass,
        String schoolClassName,
        Long idStudent,
        String studentName,
        String userName,
        @JsonFormat(pattern = "dd/MM/yyyy - HH:mm") LocalDateTime registerDate,
        String category,
        String type,
        String description,
        Status status,
        @JsonFormat(pattern = "dd/MM/yyyy - HH:mm") LocalDateTime updateDate,
        Boolean deleted) {

    public IncidentResponse(Incident incident) {
        this(incident.getId(),
                incident.getSchoolClass().getId(),
                incident.getSchoolClass().getName(),
                incident.getStudent().getId(),
                incident.getStudent().getName(),
                incident.getUser().getName(),
                incident.getRegisterDate(),
                incident.getCategory().getName(),
                incident.getType().getName(),
                incident.getDescription(),
                incident.getStatus(),
                incident.getUpdateDate(),
                incident.getDeleted());
    }
}

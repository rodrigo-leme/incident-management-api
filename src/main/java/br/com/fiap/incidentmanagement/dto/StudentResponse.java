package br.com.fiap.incidentmanagement.dto;

import br.com.fiap.incidentmanagement.entity.Student;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDate;

public record StudentResponse(
        Long id,
        String name,
        @JsonFormat(pattern = "dd/MM/yyyy") LocalDate birthDate,
        String imageUrl,
        Boolean active) {

    public StudentResponse(Student student) {
        this(student.getId(), student.getName(), student.getBirthDate(), student.getImage(), student.getActive());
    }
}

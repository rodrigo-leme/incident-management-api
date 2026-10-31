package br.com.fiap.incidentmanagement.dto;

import br.com.fiap.incidentmanagement.entity.SchoolClass;
import br.com.fiap.incidentmanagement.entity.Semester;
import br.com.fiap.incidentmanagement.entity.Shift;

public record SchoolClassResponse(Long id, String name, Shift shift, Integer year, Semester semester, Boolean canceled) {

    public SchoolClassResponse(SchoolClass schoolClass) {
        this(schoolClass.getId(), schoolClass.getName(), schoolClass.getShift(), schoolClass.getYear(),
                schoolClass.getSemester(), schoolClass.getCanceled());
    }
}

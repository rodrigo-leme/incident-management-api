package br.com.fiap.incidentmanagement.service;

import br.com.fiap.incidentmanagement.dto.SchoolClassCreateData;
import br.com.fiap.incidentmanagement.dto.SchoolClassResponse;
import br.com.fiap.incidentmanagement.dto.SchoolClassUpdateData;
import br.com.fiap.incidentmanagement.dto.StudentResponse;
import br.com.fiap.incidentmanagement.entity.SchoolClass;
import br.com.fiap.incidentmanagement.entity.Student;
import br.com.fiap.incidentmanagement.repository.SchoolClassRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SchoolClassService {

    @Autowired
    private SchoolClassRepository repository;

    @Autowired
    private StudentService studentService;

    @Transactional
    public SchoolClassResponse create(SchoolClassCreateData data) {
        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setName(data.name());
        schoolClass.setShift(data.shift());
        schoolClass.setYear(data.year());
        schoolClass.setSemester(data.semester());
        schoolClass.setCanceled(false);

        repository.save(schoolClass);
        return new SchoolClassResponse(schoolClass);
    }

    public Page<SchoolClassResponse> findAll(Boolean canceled, Pageable pageable) {
        return repository.findAllByCanceled(canceled, pageable).map(SchoolClassResponse::new);
    }

    public SchoolClassResponse findById(Long id) {
        return new SchoolClassResponse(findSchoolClass(id));
    }

    @Transactional
    public SchoolClassResponse update(SchoolClassUpdateData data) {
        SchoolClass schoolClass = findSchoolClass(data.id());
        schoolClass.setName(data.name());
        schoolClass.setShift(data.shift());
        schoolClass.setYear(data.year());
        schoolClass.setSemester(data.semester());
        return new SchoolClassResponse(schoolClass);
    }

    @Transactional
    public void cancel(Long id) {
        SchoolClass schoolClass = findSchoolClass(id);
        schoolClass.setCanceled(true);
    }

    @Transactional
    public void addStudent(Long id, Long idStudent) {
        SchoolClass schoolClass = findSchoolClass(id);
        Student student = studentService.findStudent(idStudent);

        if (schoolClass.getStudents().contains(student)) {
            throw new IllegalArgumentException("Aluno já vinculado a esta turma.");
        }

        schoolClass.getStudents().add(student);
    }

    @Transactional(readOnly = true)
    public List<StudentResponse> findStudents(Long id) {
        return findSchoolClass(id).getStudents().stream().map(StudentResponse::new).toList();
    }

    public SchoolClass findSchoolClass(Long id) {
        return repository.findById(id).orElseThrow(EntityNotFoundException::new);
    }
}

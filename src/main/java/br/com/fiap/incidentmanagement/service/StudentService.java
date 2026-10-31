package br.com.fiap.incidentmanagement.service;

import br.com.fiap.incidentmanagement.dto.StudentCreateData;
import br.com.fiap.incidentmanagement.dto.StudentResponse;
import br.com.fiap.incidentmanagement.dto.StudentUpdateData;
import br.com.fiap.incidentmanagement.entity.Student;
import br.com.fiap.incidentmanagement.repository.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Service
public class StudentService {

    @Autowired
    private StudentRepository repository;

    @Transactional
    public StudentResponse create(StudentCreateData data) {
        Student student = new Student();
        student.setName(data.name());
        student.setBirthDate(data.birthDate());
        student.setActive(true);

        repository.save(student);
        return new StudentResponse(student);
    }

    public Page<StudentResponse> findAll(Pageable pageable) {
        return repository.findAllByActiveTrue(pageable).map(StudentResponse::new);
    }

    public StudentResponse findById(Long id) {
        return new StudentResponse(findStudent(id));
    }

    @Transactional
    public StudentResponse update(StudentUpdateData data) {
        Student student = findStudent(data.id());
        student.setName(data.name());
        student.setBirthDate(data.birthDate());
        return new StudentResponse(student);
    }

    @Transactional
    public StudentResponse uploadImage(Long id, MultipartFile image) throws IOException {
        Student student = findStudent(id);
        // A foto é salva no banco no formato "data URL", que pode ser usado direto no <img src="">
        String base64 = Base64.getEncoder().encodeToString(image.getBytes());
        student.setImage("data:" + image.getContentType() + ";base64," + base64);
        return new StudentResponse(student);
    }

    @Transactional
    public void inactivate(Long id) {
        Student student = findStudent(id);
        student.setActive(false);
    }

    public Student findStudent(Long id) {
        return repository.findById(id).orElseThrow(EntityNotFoundException::new);
    }
}

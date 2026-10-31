package br.com.fiap.incidentmanagement.controller;

import br.com.fiap.incidentmanagement.dto.SchoolClassCreateData;
import br.com.fiap.incidentmanagement.dto.SchoolClassResponse;
import br.com.fiap.incidentmanagement.dto.SchoolClassUpdateData;
import br.com.fiap.incidentmanagement.dto.StudentResponse;
import br.com.fiap.incidentmanagement.entity.Semester;
import br.com.fiap.incidentmanagement.entity.Shift;
import br.com.fiap.incidentmanagement.service.SchoolClassService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/schoolclasses")
public class SchoolClassController {

    @Autowired
    private SchoolClassService service;

    @PostMapping
    public ResponseEntity<SchoolClassResponse> create(@RequestBody @Valid SchoolClassCreateData data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(data));
    }

    @GetMapping
    public ResponseEntity<Page<SchoolClassResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(service.findAll(false, pageable));
    }

    @GetMapping("/canceled")
    public ResponseEntity<Page<SchoolClassResponse>> findCanceled(Pageable pageable) {
        return ResponseEntity.ok(service.findAll(true, pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SchoolClassResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/shifts")
    public ResponseEntity<Shift[]> findShifts() {
        return ResponseEntity.ok(Shift.values());
    }

    @GetMapping("/semesters")
    public ResponseEntity<Semester[]> findSemesters() {
        return ResponseEntity.ok(Semester.values());
    }

    @PutMapping
    public ResponseEntity<SchoolClassResponse> update(@RequestBody @Valid SchoolClassUpdateData data) {
        return ResponseEntity.ok(service.update(data));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable Long id) {
        service.cancel(id);
        return ResponseEntity.noContent().build();
    }

    // Vincula um aluno à turma
    @PostMapping("/{id}/students/{idStudent}")
    public ResponseEntity<Void> addStudent(@PathVariable Long id, @PathVariable Long idStudent) {
        service.addStudent(id, idStudent);
        return ResponseEntity.noContent().build();
    }

    // Lista os alunos vinculados à turma
    @GetMapping("/{id}/students")
    public ResponseEntity<List<StudentResponse>> findStudents(@PathVariable Long id) {
        return ResponseEntity.ok(service.findStudents(id));
    }
}

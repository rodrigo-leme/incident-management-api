package br.com.fiap.incidentmanagement.controller;

import br.com.fiap.incidentmanagement.dto.StudentCreateData;
import br.com.fiap.incidentmanagement.dto.StudentResponse;
import br.com.fiap.incidentmanagement.dto.StudentUpdateData;
import br.com.fiap.incidentmanagement.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService service;

    @PostMapping
    public ResponseEntity<StudentResponse> create(@RequestBody @Valid StudentCreateData data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(data));
    }

    @GetMapping
    public ResponseEntity<Page<StudentResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @PutMapping
    public ResponseEntity<StudentResponse> update(@RequestBody @Valid StudentUpdateData data) {
        return ResponseEntity.ok(service.update(data));
    }

    @PatchMapping("/{id}/image")
    public ResponseEntity<StudentResponse> uploadImage(@PathVariable Long id,
                                                       @RequestParam("image") MultipartFile image) throws IOException {
        return ResponseEntity.ok(service.uploadImage(id, image));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inactivate(@PathVariable Long id) {
        service.inactivate(id);
        return ResponseEntity.noContent().build();
    }
}

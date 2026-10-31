package br.com.fiap.incidentmanagement.controller;

import br.com.fiap.incidentmanagement.dto.*;
import br.com.fiap.incidentmanagement.entity.User;
import br.com.fiap.incidentmanagement.service.IncidentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/incidents")
public class IncidentController {

    @Autowired
    private IncidentService service;

    @PostMapping
    public ResponseEntity<IncidentResponse> create(@RequestBody @Valid IncidentCreateData data,
                                                   @AuthenticationPrincipal User loggedUser) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(data, loggedUser));
    }

    @GetMapping
    public ResponseEntity<Page<IncidentResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<IncidentResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    // Consulta com filtros por aluno, turma, categoria, tipo e período (datas no formato dd/MM/yyyy)
    @GetMapping("/search")
    public ResponseEntity<List<IncidentResponse>> search(
            @RequestParam(required = false) Long idStudent,
            @RequestParam(required = false) Long idSchoolClass,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(pattern = "dd/MM/yyyy") LocalDate endDate) {
        return ResponseEntity.ok(service.search(idStudent, idSchoolClass, category, type, startDate, endDate));
    }

    @PutMapping
    public ResponseEntity<IncidentResponse> update(@RequestBody @Valid IncidentUpdateData data,
                                                   @AuthenticationPrincipal User loggedUser) {
        return ResponseEntity.ok(service.update(data, loggedUser));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal User loggedUser) {
        service.delete(id, loggedUser);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status")
    public ResponseEntity<List<StatusData>> findStatuses() {
        return ResponseEntity.ok(service.findStatuses());
    }

    @PatchMapping("/status")
    public ResponseEntity<IncidentResponse> updateStatus(@RequestBody @Valid IncidentStatusData data) {
        return ResponseEntity.ok(service.updateStatus(data));
    }

    // Listagens por status: waiting, progressing, active, solved, unsolved, closed
    @GetMapping("/status/{group}")
    public ResponseEntity<Page<IncidentResponse>> findByStatus(@PathVariable String group, Pageable pageable) {
        return ResponseEntity.ok(service.findByStatus(group, pageable));
    }

    // Dados do dashboard
    @GetMapping("/summary")
    public ResponseEntity<SummaryData> summary() {
        return ResponseEntity.ok(service.summary());
    }
}

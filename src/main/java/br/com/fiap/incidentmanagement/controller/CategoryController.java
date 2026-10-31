package br.com.fiap.incidentmanagement.controller;

import br.com.fiap.incidentmanagement.dto.CategoryData;
import br.com.fiap.incidentmanagement.dto.IncidentTypeData;
import br.com.fiap.incidentmanagement.service.CategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/incidents")
public class CategoryController {

    @Autowired
    private CategoryService service;

    @GetMapping("/categories")
    public ResponseEntity<List<String>> findCategories() {
        return ResponseEntity.ok(service.findCategories());
    }

    @PostMapping("/categories")
    public ResponseEntity<Void> createCategory(@RequestBody @Valid CategoryData data) {
        service.createCategory(data);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/types/{category}")
    public ResponseEntity<List<String>> findTypes(@PathVariable String category) {
        return ResponseEntity.ok(service.findTypes(category));
    }

    @PostMapping("/types")
    public ResponseEntity<Void> createType(@RequestBody @Valid IncidentTypeData data) {
        service.createType(data);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}

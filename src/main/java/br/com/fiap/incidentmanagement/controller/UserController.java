package br.com.fiap.incidentmanagement.controller;

import br.com.fiap.incidentmanagement.dto.*;
import br.com.fiap.incidentmanagement.entity.User;
import br.com.fiap.incidentmanagement.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;

    @PostMapping
    public ResponseEntity<UserResponse> create(@RequestBody @Valid UserCreateData data) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(data));
    }

    @GetMapping
    public ResponseEntity<Page<UserResponse>> findAll(Pageable pageable) {
        return ResponseEntity.ok(service.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(service.findById(id));
    }

    @GetMapping("/roles")
    public ResponseEntity<List<String>> findRoles() {
        return ResponseEntity.ok(service.findRoles());
    }

    @PutMapping
    public ResponseEntity<UserResponse> update(@RequestBody @Valid UserUpdateData data) {
        return ResponseEntity.ok(service.update(data));
    }

    @PatchMapping
    public ResponseEntity<UserResponse> updateRole(@RequestBody @Valid UserRoleData data) {
        return ResponseEntity.ok(service.updateRole(data));
    }

    @PatchMapping("/password")
    public ResponseEntity<Void> changePassword(@RequestBody @Valid PasswordData data,
                                               @AuthenticationPrincipal User loggedUser) {
        service.changePassword(data, loggedUser);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> inactivate(@PathVariable Long id) {
        service.inactivate(id);
        return ResponseEntity.noContent().build();
    }
}

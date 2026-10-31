package br.com.fiap.incidentmanagement.service;

import br.com.fiap.incidentmanagement.dto.*;
import br.com.fiap.incidentmanagement.entity.Role;
import br.com.fiap.incidentmanagement.entity.User;
import br.com.fiap.incidentmanagement.repository.RoleRepository;
import br.com.fiap.incidentmanagement.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public UserResponse create(UserCreateData data) {
        if (userRepository.existsByLogin(data.login())) {
            throw new IllegalArgumentException("Login já cadastrado.");
        }

        User user = new User();
        user.setName(data.name());
        user.setLogin(data.login());
        user.setPassword(passwordEncoder.encode(data.password()));
        user.setRole(findRole(data.role()));
        user.setActive(true);

        userRepository.save(user);
        return new UserResponse(user);
    }

    public Page<UserResponse> findAll(Pageable pageable) {
        return userRepository.findAllByActiveTrue(pageable).map(UserResponse::new);
    }

    public UserResponse findById(Long id) {
        return new UserResponse(findUser(id));
    }

    public List<String> findRoles() {
        return roleRepository.findAll().stream().map(Role::getName).toList();
    }

    @Transactional
    public UserResponse update(UserUpdateData data) {
        User user = findUser(data.id());
        user.setName(data.name());
        user.setLogin(data.login());
        return new UserResponse(user);
    }

    @Transactional
    public UserResponse updateRole(UserRoleData data) {
        User user = findUser(data.id());
        user.setRole(findRole(data.role()));
        return new UserResponse(user);
    }

    @Transactional
    public void changePassword(PasswordData data, User loggedUser) {
        User user = findUser(loggedUser.getId());

        if (!passwordEncoder.matches(data.oldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Senha atual incorreta.");
        }

        user.setPassword(passwordEncoder.encode(data.newPassword()));
    }

    @Transactional
    public void inactivate(Long id) {
        User user = findUser(id);
        user.setActive(false);
    }

    private User findUser(Long id) {
        return userRepository.findById(id).orElseThrow(EntityNotFoundException::new);
    }

    private Role findRole(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new IllegalArgumentException("Perfil não encontrado: " + name));
    }
}

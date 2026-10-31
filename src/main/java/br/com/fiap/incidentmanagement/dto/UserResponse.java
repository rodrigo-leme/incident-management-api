package br.com.fiap.incidentmanagement.dto;

import br.com.fiap.incidentmanagement.entity.User;

public record UserResponse(Long id, String name, String login, String role, Boolean active) {

    public UserResponse(User user) {
        this(user.getId(), user.getName(), user.getLogin(), user.getRole().getName(), user.getActive());
    }
}

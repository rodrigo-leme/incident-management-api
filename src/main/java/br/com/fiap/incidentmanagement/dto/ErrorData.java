package br.com.fiap.incidentmanagement.dto;

import org.springframework.validation.FieldError;

public record ErrorData(String field, String message) {

    public ErrorData(FieldError error) {
        this(error.getField(), error.getDefaultMessage());
    }
}

package com.matheuszanatta.desafiovotacao.exception.handler.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidationError {

    private String field;
    private String message;

    public ValidationError(org.springframework.validation.FieldError fieldError) {
        this.field = fieldError.getField();
        this.message = fieldError.getDefaultMessage();
    }
}

package com.matheuszanatta.desafiovotacao.exception.handler.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@Setter
public class ApiValidationError {

    private int status;
    private String message;
    private List<ValidationError> errors;
    private LocalDateTime timestamp;
}

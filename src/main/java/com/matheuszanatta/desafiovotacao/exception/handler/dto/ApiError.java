package com.matheuszanatta.desafiovotacao.exception.handler.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
public class ApiError {

    private int status;
    private String message;
    private LocalDateTime timestamp;
}

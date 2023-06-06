package com.matheuszanatta.desafiovotacao.exception.handler;

import com.matheuszanatta.desafiovotacao.exception.RecursoNaoEncontradoException;
import com.matheuszanatta.desafiovotacao.exception.RegraNegocioException;
import com.matheuszanatta.desafiovotacao.exception.handler.dto.ApiError;
import com.matheuszanatta.desafiovotacao.exception.handler.dto.ApiValidationError;
import com.matheuszanatta.desafiovotacao.exception.handler.dto.ValidationError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import static java.time.LocalDateTime.now;
import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
@Slf4j
public class RestExceptionHandler {

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    @ResponseStatus(NOT_FOUND)
    public ResponseEntity<Object> handleRecursoNaoEncontradoException(RecursoNaoEncontradoException exception) {
        log.error(exception.getMessage());
        return buildApiError(NOT_FOUND, exception);
    }

    @ExceptionHandler(RegraNegocioException.class)
    @ResponseStatus(UNPROCESSABLE_ENTITY)
    public ResponseEntity<Object> handleForbiddenActionException(RegraNegocioException exception) {
        log.error(exception.getMessage());
        return buildApiError(UNPROCESSABLE_ENTITY, exception);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException exception) {
        log.error(exception.getMessage());
        return buildApiError(BAD_REQUEST, exception);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        log.error(exception.getMessage());
        return buildApiValidationError(BAD_REQUEST, exception);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(BAD_REQUEST)
    public ResponseEntity<Object> handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        log.error(exception.getMessage());
        return buildApiError(BAD_REQUEST, "Erro de parse no corpo da requisição");
    }

    private ResponseEntity<Object> buildApiError(HttpStatus status, Exception exception) {
        return buildResponseEntity(
                ApiError.builder()
                        .status(status.value())
                        .message(exception.getMessage())
                        .timestamp(now())
                        .build());
    }

    private ResponseEntity<Object> buildApiError(HttpStatus status, String message) {
        return buildResponseEntity(
                ApiError.builder()
                        .status(status.value())
                        .message(message)
                        .timestamp(now())
                        .build());
    }

    private ResponseEntity<Object> buildApiValidationError(HttpStatus status, MethodArgumentNotValidException exception) {
        var fieldErrors = exception.getBindingResult().getFieldErrors();
        var validationErrors = fieldErrors.stream().map(ValidationError::new).toList();
        return buildResponseEntity(
                ApiValidationError.builder()
                        .status(status.value())
                        .message("Erro de validação")
                        .timestamp(now())
                        .errors(validationErrors)
                        .build());
    }

    private ResponseEntity<Object> buildResponseEntity(ApiValidationError apiValidationError) {
        return new ResponseEntity<>(apiValidationError, HttpStatusCode.valueOf(apiValidationError.getStatus()));
    }

    private ResponseEntity<Object> buildResponseEntity(ApiError apiError) {
        return new ResponseEntity<>(apiError, HttpStatusCode.valueOf(apiError.getStatus()));
    }
}

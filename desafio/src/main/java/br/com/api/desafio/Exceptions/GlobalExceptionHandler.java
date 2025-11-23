package br.com.api.desafio.Exceptions;

import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    // 400 — ValidationException personalizada
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(ValidationException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(
                ErrorResponse.simple(status.value(), status.getReasonPhrase(), ex.getMessage())
        );
    }

    // 404 — NotFoundException
    @ExceptionHandler(ChangeSetPersister.NotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFoundException(ChangeSetPersister.NotFoundException ex) {
        HttpStatus status = HttpStatus.NOT_FOUND;
        return ResponseEntity.status(status).body(
                ErrorResponse.simple(status.value(), status.getReasonPhrase(), ex.getMessage())
        );
    }

    // 400 — IllegalArgumentException
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        return ResponseEntity.status(status).body(
                ErrorResponse.simple(status.value(), status.getReasonPhrase(), ex.getMessage())
        );
    }

    // 400 — Bean Validation (@Valid)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleBeanValidation(MethodArgumentNotValidException ex) {

        Map<String, String> fieldErrors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(error ->
                fieldErrors.put(error.getField(), error.getDefaultMessage())
        );

        HttpStatus status = HttpStatus.BAD_REQUEST;

        return ResponseEntity.status(status).body(
                ErrorResponse.withFields(
                        status.value(),
                        status.getReasonPhrase(),
                        "Erro de validação nos campos enviados.",
                        fieldErrors
                )
        );
    }

    // 500 — erros inesperados
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        ex.printStackTrace();

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        return ResponseEntity.status(status).body(
                ErrorResponse.simple(status.value(), status.getReasonPhrase(), "Erro interno no servidor")
        );
    }
}

package br.com.api.desafio.Exceptions;

import java.time.Instant;
import java.util.Map;

public record ErrorResponse(
        Instant timestamp,
        int status,
        String error,
        String message,
        Map<String, String> fields
) {
    public static ErrorResponse simple(int status, String error, String message) {
        return new ErrorResponse(
                Instant.now(),
                status,
                error,
                message,
                null
        );
    }

    public static ErrorResponse withFields(int status, String error, String message, Map<String, String> fields) {
        return new ErrorResponse(
                Instant.now(),
                status,
                error,
                message,
                fields
        );
    }
}

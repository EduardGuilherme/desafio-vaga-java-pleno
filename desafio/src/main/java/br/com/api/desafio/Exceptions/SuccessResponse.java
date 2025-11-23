package br.com.api.desafio.Exceptions;

import java.time.Instant;
import java.util.Map;

public record SuccessResponse(
        Instant timestamp,
        int status,
        String message,
        Object data,
        Map<String, Object> meta
) {
    public static SuccessResponse ok(String message, Object data) {
        return new SuccessResponse(
                Instant.now(),
                200,
                message,
                data,
                null
        );
    }

    public static SuccessResponse created(String message, Object data) {
        return new SuccessResponse(
                Instant.now(),
                201,
                message,
                data,
                null
        );
    }

    public static SuccessResponse withMeta(String message, Object data, Map<String, Object> meta) {
        return new SuccessResponse(
                Instant.now(),
                200,
                message,
                data,
                meta
        );
    }
}

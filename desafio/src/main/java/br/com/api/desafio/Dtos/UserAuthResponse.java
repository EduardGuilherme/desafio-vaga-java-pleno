package br.com.api.desafio.Dtos;

import java.util.UUID;

public record UserAuthResponse(
        UUID id,
        String email,
        String name,
        String department
) {
}

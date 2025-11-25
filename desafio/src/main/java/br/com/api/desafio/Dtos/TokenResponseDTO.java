package br.com.api.desafio.Dtos;

import java.util.UUID;

public record TokenResponseDTO(
        String token,
        String type,
        UUID userId,
        String email,
        String name
) {

}

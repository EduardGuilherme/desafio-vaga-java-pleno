package br.com.api.desafio.Dtos;

public record UserAuthResponse(
        String id,
        String email,
        String name,
        String department
) {
}

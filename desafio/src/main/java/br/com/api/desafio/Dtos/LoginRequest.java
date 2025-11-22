package br.com.api.desafio.Dtos;

public record LoginRequest(
        String email,
        String password
) {
}

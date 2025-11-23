package br.com.api.desafio.Dtos;

public record RenewRequestDTO(
        String justification,
        boolean urgent
) {
}

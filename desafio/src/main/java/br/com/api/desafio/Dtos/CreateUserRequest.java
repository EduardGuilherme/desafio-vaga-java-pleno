package br.com.api.desafio.Dtos;

import br.com.api.desafio.Enums.Departament;

public record CreateUserRequest(
        String name,
        String email,
        String password,
        Departament departament
) {
}

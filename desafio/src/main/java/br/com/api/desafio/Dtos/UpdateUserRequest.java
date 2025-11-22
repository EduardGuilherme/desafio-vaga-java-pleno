package br.com.api.desafio.Dtos;

import br.com.api.desafio.Enums.Departament;

public record UpdateUserRequest(
        String name,
        String email,
        String password,
        Departament departament
) {

}

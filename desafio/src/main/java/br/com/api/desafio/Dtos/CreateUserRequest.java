package br.com.api.desafio.Dtos;

import br.com.api.desafio.Enums.Departament;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateUserRequest(
        @NotBlank(message = "O nome é obrigatório")
        String name,
        @NotBlank(message = "O email é obrigatório")
        @Email(message = "Formato de e-mail inválido")
        String email,
        @NotBlank(message = "A senha é obrigatória")
        String password,
        @NotNull(message = "O departamento é obrigatório")
        Departament departament
) {
}

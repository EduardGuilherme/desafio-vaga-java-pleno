package br.com.api.desafio.users;

import br.com.api.desafio.Dtos.CreateUserRequest;
import br.com.api.desafio.Enums.Departament;
import br.com.api.desafio.Model.User;
import br.com.api.desafio.Repository.UserRepository;
import br.com.api.desafio.Services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService service;

    // ------------------------------
    // TESTE 1 — Criar usuário com sucesso
    // ------------------------------
    @Test
    void shouldCreateUserSuccessfully() {

        CreateUserRequest request = new CreateUserRequest(
                "Eduardo",
                "edu@example.com",
                "123456",
                Departament.TI
        );

        when(repository.findByEmail("edu@example.com"))
                .thenReturn(Optional.empty());

        User saved = User.builder()
                .id(UUID.randomUUID())
                .name("Eduardo")
                .email("edu@example.com")
                .password("$2a$10$encryptedPassword")
                .department(Departament.TI)
                .build();

        when(repository.save(any(User.class))).thenReturn(saved);

        User result = service.createUser(request);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isNotNull();
        assertThat(result.getEmail()).isEqualTo("edu@example.com");

        verify(repository, times(1)).save(any(User.class));
    }

    // ------------------------------
    // TESTE 2 — Email já existe
    // ------------------------------
    @Test
    void shouldNotCreateUserWhenEmailAlreadyExists() {

        CreateUserRequest request = new CreateUserRequest(
                "Edu",
                "edu@example.com",
                "123456",
                Departament.TI
        );

        when(repository.findByEmail("edu@example.com"))
                .thenReturn(Optional.of(new User()));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.createUser(request)
        );

        assertThat(ex.getMessage()).isEqualTo("E-mail já está em uso");
    }

    // ------------------------------
    // TESTE 3 — Senha criptografada
    // ------------------------------
    @Test
    void shouldEncryptPasswordWhenCreatingUser() {

        CreateUserRequest request = new CreateUserRequest(
                "Eduardo",
                "edu@example.com",
                "123456",
                Departament.FINANCEIRO
        );

        when(repository.findByEmail("edu@example.com"))
                .thenReturn(Optional.empty());

        User saved = User.builder()
                .id(UUID.randomUUID())
                .name("Eduardo")
                .email("edu@example.com")
                .password("$2a$10$encryptedPasswordExample")
                .department(Departament.FINANCEIRO)
                .build();

        when(repository.save(any(User.class))).thenReturn(saved);

        User result = service.createUser(request);

        assertThat(result.getPassword()).isNotEqualTo("123456");
        assertThat(result.getPassword()).startsWith("$2a$");
    }

    // ------------------------------
    // TESTE 4 — Criar usuário com departamento enviado
    // ------------------------------
    @Test
    void shouldCreateUserWithProvidedDepartment() {

        CreateUserRequest request = new CreateUserRequest(
                "Eduardo",
                "edu@example.com",
                "123456",
                Departament.FINANCEIRO
        );

        when(repository.findByEmail("edu@example.com"))
                .thenReturn(Optional.empty());

        User saved = User.builder()
                .id(UUID.randomUUID())
                .name("Eduardo")
                .email("edu@example.com")
                .password("encryptedPass")
                .department(Departament.FINANCEIRO)
                .build();

        when(repository.save(any(User.class))).thenReturn(saved);

        User result = service.createUser(request);

        assertThat(result.getDepartment())
                .isEqualTo(Departament.FINANCEIRO);
    }

    // ------------------------------
    // TESTE 5 — Email inválido
    // ------------------------------
    @Test
    void shouldNotCreateUserWithInvalidEmail() {

        CreateUserRequest request = new CreateUserRequest(
                "Eduardo",
                "email_invalido",
                "123456",
                Departament.FINANCEIRO
        );

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.createUser(request)
        );

        assertThat(ex.getMessage()).isEqualTo("Email Invalido");
    }

    // ------------------------------
    // TESTE 6 — Falta de campos obrigatórios
    // ------------------------------
    @Test
    void shouldNotCreateUserWithMissingRequiredFields() {

        CreateUserRequest request = new CreateUserRequest(
                "",
                "",
                "",
                Departament.FINANCEIRO
        );

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.createUser(request)
        );

        assertThat(ex.getMessage())
                .isEqualTo("campos Obrigadotios ausentes");
    }
}

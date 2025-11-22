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

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository repository;

    @InjectMocks
    private UserService service;

    @Test
    void shouldCreateUserSuccessfully() {
        // Arrange (preparação)
        CreateUserRequest request = new CreateUserRequest(
                "Eduardo",
                "edu@example.com",
                "123456",
                Departament.TI
        );

        User savedUser = new User("1L", "Eduardo", "edu@example.com", "123456", Departament.TI);

        when(repository.findByEmail("edu@example.com"))
                .thenReturn(Optional.empty());

        when(repository.save(any(User.class)))
                .thenReturn(savedUser);

        // Act (ação)
        User result = service.createUser(request);

        // Assert (validação)
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo("1L");
        assertThat(result.getEmail()).isEqualTo("edu@example.com");

        verify(repository, times(1)).save(any(User.class));
    }

    @Test
    void shouldNotCreateUserWhenEmailAlreadyExists() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest(
                "Edu",
                "edu@example.com",
                "123456",
                Departament.TI
        );

        when(repository.findByEmail("edu@example.com"))
                .thenReturn(Optional.of(new User()));

        // Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.createUser(request)
        );

        assertThat(exception.getMessage())
                .isEqualTo("E-mail já está em uso");
    }

    @Test
    void shouldEncryptPasswordWhenCreatingUser() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest(
                "Eduardo",
                "edu@example.com",
                "123456",
                Departament.FINANCEIRO
        );

        when(repository.findByEmail("edu@example.com"))
                .thenReturn(Optional.empty());

        // Criando um usuário que teria sido salvo
        User savedUser = new User(
                "1L",
                "Eduardo",
                "edu@example.com",
                "$2a$10$encryptedpasswordexample",
                Departament.TI
        );

        when(repository.save(any(User.class))).thenReturn(savedUser);

        // Act
        User result = service.createUser(request);

        // Assert
        assertThat(result.getPassword()).isNotEqualTo("123456");
        assertThat(result.getPassword()).startsWith("$2a$"); // padrão do BCrypt
    }

    @Test
    void shouldCreateUserWithProvidedDepartment() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest(
                "Eduardo",
                "edu@example.com",
                "123456",
                Departament.FINANCEIRO
        );

        when(repository.findByEmail("edu@example.com"))
                .thenReturn(Optional.empty());

        User savedUser = new User(
                "1L",
                "Eduardo",
                "edu@example.com",
                "encryptedPass",
                Departament.FINANCEIRO
        );

        when(repository.save(any(User.class))).thenReturn(savedUser);

        // Act
        User result = service.createUser(request);

        // Assert
        assertThat(result.getDepartment()).isEqualTo(Departament.FINANCEIRO);
    }

    @Test
    void shouldNotCreateUserWithInvalidEmail() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest(
                "Eduardo",
                "eduardo2008@gmail.com",
                "123456",
                Departament.FINANCEIRO
        );

        // Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.createUser(request)
        );

        assertThat(exception.getMessage())
                .isEqualTo("E-mail inválido");
    }

    @Test
    void shouldNotCreateUserWithMissingRequiredFields() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest(
                "",         // nome inválido
                "",         // email inválido
                "",          // senha inválida
                Departament.FINANCEIRO
        );

        // Act + Assert
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> service.createUser(request)
        );

        assertThat(exception.getMessage())
                .isEqualTo("Campos obrigatórios ausentes");
    }

    /*@Test
    void shouldCreateUserSuccessfully() {
        // Arrange
        User user = new User();
        user.setName("Eduardo");
        user.setEmail("eduardo@email.com");
        user.setPassword("123456");

        when(repository.save(user)).thenReturn(user);

        // Act
        User result = service.createUser(user);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Eduardo");
        assertThat(result.getEmail()).isEqualTo("eduardo@email.com");
    }*/
}

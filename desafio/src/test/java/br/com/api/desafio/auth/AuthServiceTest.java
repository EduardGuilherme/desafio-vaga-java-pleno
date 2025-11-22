package br.com.api.desafio.auth;

import br.com.api.desafio.Auth.AuthService;
import br.com.api.desafio.Dtos.LoginRequest;
import br.com.api.desafio.Dtos.UserAuthResponse;
import br.com.api.desafio.Enums.Departament;
import br.com.api.desafio.Model.User;
import br.com.api.desafio.Repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Optional;

public class AuthServiceTest {

    private UserRepository userRepository;
    private AuthService authService;
    private BCryptPasswordEncoder passwordEncoder;

    @BeforeEach
    void setup(){
        userRepository = mock(UserRepository.class);
        passwordEncoder = new BCryptPasswordEncoder();
        authService = new AuthService(userRepository,passwordEncoder);
    }

    @Test
    void mustAuthenticateUserWithValidCredentials(){
        // Arrange
        LoginRequest request = new LoginRequest("teste@teste.com", "123456");

        String senhaCripto = passwordEncoder.encode("123456");

        User user = new User(
                "1L",
                "Eduardo",
                "teste@teste.com",
                senhaCripto,
                Departament.TI
        );

        when(userRepository.findByEmail("teste@teste.com"))
                .thenReturn(Optional.of(user));

        // Act
        UserAuthResponse result = authService.login(request);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.email()).isEqualTo("teste@teste.com");
        assertThat(result.name()).isEqualTo("Eduardo");
        assertThat(result.department()).isEqualTo("TI");
    }

    @Test
    void shouldFailWhenUserDoesNotExist() {
        LoginRequest request = new LoginRequest("naoexiste@teste.com", "123456");

        when(userRepository.findByEmail("naoexiste@teste.com"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Usuário não encontrado");
    }

    @Test
    void shouldFailWhenIncorrectPassword() {
        LoginRequest request = new LoginRequest("teste@teste.com", "senhaErrada");

        String senhaCriptografada = passwordEncoder.encode("senhaCorreta");

        User user = new User(
                "1L",
                "Eduardo",
                "teste@teste.com",
                senhaCriptografada,
                Departament.TI
        );

        when(userRepository.findByEmail("teste@teste.com"))
                .thenReturn(Optional.of(user));

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Senha inválida");
    }
}

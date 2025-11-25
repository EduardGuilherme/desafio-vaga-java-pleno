package br.com.api.desafio.auth;

import br.com.api.desafio.Auth.AuthService;
import br.com.api.desafio.Dtos.LoginRequest;
import br.com.api.desafio.Dtos.TokenResponseDTO;
import br.com.api.desafio.Dtos.UserAuthResponse;
import br.com.api.desafio.Enums.Departament;
import br.com.api.desafio.Exceptions.InvalidPasswordException;
import br.com.api.desafio.Exceptions.UserNotFoundException;
import br.com.api.desafio.Model.User;
import br.com.api.desafio.Repository.UserRepository;
import br.com.api.desafio.Security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import java.util.Optional;
import java.util.UUID;

public class AuthServiceTest {

    private UserRepository userRepository;
    private AuthService authService;
    private BCryptPasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;

    @BeforeEach
    void setup(){
        userRepository = mock(UserRepository.class);
        passwordEncoder = new BCryptPasswordEncoder();
        authService = new AuthService(userRepository,passwordEncoder,jwtUtil);
    }

    @Test
    void mustAuthenticateUserWithValidCredentials(){

        LoginRequest request = new LoginRequest("teste@teste.com", "123456");

        String senhaCripto = passwordEncoder.encode("123456");

        User user = User.builder()
                .id(UUID.randomUUID())
                .name("Eduardo")
                .email("teste@teste.com")
                .password(senhaCripto)
                .department(Departament.TI)
                .build();

        when(userRepository.findByEmail("teste@teste.com"))
                .thenReturn(Optional.of(user));

        // Act
        TokenResponseDTO result = authService.login(request);

        // Assert
        assertThat(result).isNotNull();
        assertThat(result.email()).isEqualTo("teste@teste.com");
        assertThat(result.name()).isEqualTo("Eduardo");
        assertThat(result.token()).isEqualTo("fake-jwt-token");

        ArgumentCaptor<String> emailCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> roleCaptor = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<UUID> idCaptor = ArgumentCaptor.forClass(UUID.class);

        verify(jwtUtil).generateToken(emailCaptor.capture(), roleCaptor.capture(), idCaptor.capture());

        assertThat(emailCaptor.getValue()).isEqualTo(user.getEmail());
        assertThat(roleCaptor.getValue()).isEqualTo("TI");
        assertThat(idCaptor.getValue()).isEqualTo(user.getId());
    }

    @Test
    void shouldFailWhenUserDoesNotExist() {
        LoginRequest request = new LoginRequest("naoexiste@teste.com", "123456");

        when(userRepository.findByEmail("naoexiste@teste.com"))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("Usuário não encontrado");
    }

    @Test
    void shouldFailWhenIncorrectPassword() {
        LoginRequest request = new LoginRequest("teste@teste.com", "senhaErrada");

        String senhaCriptografada = passwordEncoder.encode("senhaCorreta");

        User user = User.builder()
                .id(UUID.randomUUID())
                .name("Eduardo")
                .email("teste@teste.com")
                .password(senhaCriptografada)
                .department(Departament.TI)
                .build();

        when(userRepository.findByEmail("teste@teste.com"))
                .thenReturn(Optional.of(user));

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(InvalidPasswordException.class)
                .hasMessage("Senha inválida");
    }

    @Test
    void shouldValidateToken() {
        when(jwtUtil.validateToken("abc123")).thenReturn(true);

        boolean result = authService.validateToken("abc123");

        assertThat(result).isTrue();
        verify(jwtUtil, times(1)).validateToken("abc123");
    }
}

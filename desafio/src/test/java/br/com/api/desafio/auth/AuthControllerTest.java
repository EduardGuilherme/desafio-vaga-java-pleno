package br.com.api.desafio.auth;

import br.com.api.desafio.Auth.AuthService;
import br.com.api.desafio.Controller.AuthController;
import br.com.api.desafio.Dtos.LoginRequest;
import br.com.api.desafio.Dtos.TokenResponseDTO;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    public AuthControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void mustReturn200WithTokenWhenLoginForValid() {

        LoginRequest request = new LoginRequest("user@test.com", "123456");
        TokenResponseDTO token = new TokenResponseDTO("jwt-token-aqui","user", UUID.randomUUID(),"user@test.com","user");

        when(authService.login(request)).thenReturn(token);


        ResponseEntity<TokenResponseDTO> response = authController.login(request);


        assertEquals(200, response.getStatusCode().value());
        assertNotNull(response.getBody());
        assertEquals("jwt-token-aqui", response.getBody().token());
        verify(authService, times(1)).login(request);
    }

    @Test
    void deveRetornar401QuandoLoginInvalido() {

        LoginRequest request = new LoginRequest("user@test.com", "senhaErrada");

        when(authService.login(request)).thenThrow(new IllegalArgumentException("Credenciais inválidas"));


        ResponseEntity<TokenResponseDTO> response = authController.login(request);


        assertEquals(401, response.getStatusCode().value());
        assertNull(response.getBody());
        verify(authService, times(1)).login(request);
    }
}

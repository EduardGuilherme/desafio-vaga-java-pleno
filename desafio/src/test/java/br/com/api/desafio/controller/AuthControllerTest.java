package br.com.api.desafio.controller;

import br.com.api.desafio.Auth.AuthService;
import br.com.api.desafio.Controller.AuthController;
import br.com.api.desafio.Dtos.LoginRequest;
import br.com.api.desafio.Dtos.TokenResponseDTO;
import br.com.api.desafio.Dtos.UserAuthResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AuthService authService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    void deveAutenticarUsuarioComSucesso() throws Exception {

        LoginRequest request = new LoginRequest("user@test.com", "123456");

        UUID id = UUID.randomUUID();

        TokenResponseDTO response = new TokenResponseDTO(
                "fake.jwt.token",
                "Bearer",
                id,
                "user@test.com",
                "Eduardo"
        );

        when(authService.login(any(LoginRequest.class)))
                .thenReturn(response);

        mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake.jwt.token"))
                .andExpect(jsonPath("$.type").value("Bearer"))
                .andExpect(jsonPath("$.userId").value(id.toString()))
                .andExpect(jsonPath("$.email").value("user@test.com"))
                .andExpect(jsonPath("$.name").value("Eduardo"));
    }

    @Test
    void deveRetornar401QuandoCredenciaisInvalidas() throws Exception {

        LoginRequest request = new LoginRequest("user@test.com", "errada");

        when(authService.login(any(LoginRequest.class)))
                .thenThrow(new RuntimeException("Credenciais inválidas"));

        mockMvc.perform(
                        post("/auth/login")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isUnauthorized());
    }
}

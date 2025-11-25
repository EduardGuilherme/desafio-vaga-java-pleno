package br.com.api.desafio.controller;

import br.com.api.desafio.Controller.AccessRequestController;
import br.com.api.desafio.Dtos.CreateAccessRequestDTO;
import br.com.api.desafio.Dtos.ResponseAccessRequestDTO;
import br.com.api.desafio.Enums.RequestStatus;
import br.com.api.desafio.Model.AccessRequest;
import br.com.api.desafio.Model.User;
import br.com.api.desafio.Services.AccessRequestService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AccessRequestController.class)
class AccessRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AccessRequestService service;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateAccessRequestSuccessfully() throws Exception {

        UUID userId = UUID.randomUUID();

        Principal principal = () -> userId.toString();

        CreateAccessRequestDTO dto = new CreateAccessRequestDTO(
                Set.of(UUID.randomUUID()),
                "Justificação válida de mais de 20 caracteres",
                false
        );

        AccessRequest entity = AccessRequest.builder()
                .id(UUID.randomUUID())
                .createdAt(LocalDateTime.now())
                .status(RequestStatus.ATIVO)
                .justification(dto.justification())
                .urgent(false)
                .requester(User.builder()
                        .id(userId)
                        .activeModules(Set.of())
                        .build())
                .modules(Set.of())
                .protocol("SOL-20250101-9999")
                .build();

        Mockito.when(service.createRequest(any(), any())).thenReturn(entity);


        mockMvc.perform(post("/requests")
                        .principal(principal)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("ATIVO"))
                .andExpect(jsonPath("$.justification").value(dto.justification()))
                .andExpect(jsonPath("$.protocol").exists());
    }
}

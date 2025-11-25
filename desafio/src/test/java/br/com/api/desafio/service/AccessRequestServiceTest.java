package br.com.api.desafio.service;

import br.com.api.desafio.Dtos.CreateAccessRequestDTO;
import br.com.api.desafio.Repository.AccessRequestRepository;
import br.com.api.desafio.Repository.ModuleRepository;
import br.com.api.desafio.Repository.UserRepository;
import br.com.api.desafio.Services.AccessRequestService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccessRequestServiceTest {

    @Mock
    private AccessRequestRepository accessRequestRepository;

    @Mock
    private ModuleRepository moduleRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AccessRequestService service;

    @Test
    void shouldFailWhenUserNotFound() {

        UUID fakeUserId = UUID.randomUUID();

        CreateAccessRequestDTO dto = new CreateAccessRequestDTO(
                Set.of(UUID.randomUUID()),
                "Solicitação válida com mais de 20 caracteres",
                false
        );

        when(userRepository.findById(fakeUserId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> service.createRequest(dto, fakeUserId))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Usuário não encontrado");
    }
}

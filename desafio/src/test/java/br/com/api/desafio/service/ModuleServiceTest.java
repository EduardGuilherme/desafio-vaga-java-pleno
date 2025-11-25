package br.com.api.desafio.service;

import br.com.api.desafio.Dtos.ModuleResponseDTO;
import br.com.api.desafio.Enums.Departament;
import br.com.api.desafio.Model.Modules;
import br.com.api.desafio.Repository.ModuleRepository;
import br.com.api.desafio.Services.ModuleService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ModuleServiceTest {

    @Mock
    private ModuleRepository repository;

    @InjectMocks
    private ModuleService service;

    @Test
    void shouldListAllModulesSuccessfully() {

        Modules module = Modules.builder()
                .id(UUID.randomUUID())
                .name("Financeiro")
                .description("Módulo financeiro")
                .active(true)
                .allowedDepartments(Set.of(Departament.TI, Departament.FINANCEIRO))
                .incompatibleWith(Set.of())
                .build();

        when(repository.findAll()).thenReturn(List.of(module));

        List<ModuleResponseDTO> result = service.listAll();

        assertThat(result).isNotEmpty();
        assertThat(result.get(0).name()).isEqualTo("Financeiro");
        assertThat(result.get(0).description()).isEqualTo("Módulo financeiro");

        verify(repository, times(1)).findAll();
    }

    @Test
    void shouldReturnEmptyListWhenNoModulesExist() {

        when(repository.findAll()).thenReturn(List.of());

        List<ModuleResponseDTO> result = service.listAll();

        assertThat(result).isEmpty();
        verify(repository, times(1)).findAll();
    }

    @Test
    void shouldMapEntityToDTOCorrectly() {

        Modules module = Modules.builder()
                .id(UUID.randomUUID())
                .name("RH")
                .description("Acesso ao RH")
                .active(false)
                .allowedDepartments(Set.of(Departament.RH))
                .incompatibleWith(Set.of())
                .build();

        when(repository.findAll()).thenReturn(List.of(module));

        List<ModuleResponseDTO> result = service.listAll();

        ModuleResponseDTO dto = result.get(0);

        assertThat(dto.name()).isEqualTo("RH");
        assertThat(dto.description()).isEqualTo("Acesso ao RH");
        assertThat(dto.active()).isFalse();
        assertThat(dto.allowedDepartments()).contains("RH");
    }
}

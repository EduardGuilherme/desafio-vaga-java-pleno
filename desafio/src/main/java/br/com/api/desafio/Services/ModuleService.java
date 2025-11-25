package br.com.api.desafio.Services;

import br.com.api.desafio.Dtos.ModuleResponseDTO;
import br.com.api.desafio.Repository.ModuleRepository;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ModuleService {
    private final ModuleRepository moduleRepository;

    public List<ModuleResponseDTO> listAll() {
        return moduleRepository.findAll()
                .stream()
                .map(ModuleResponseDTO::fromEntity)
                .toList();
    }
}

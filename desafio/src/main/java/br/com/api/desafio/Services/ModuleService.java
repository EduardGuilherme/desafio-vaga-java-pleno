package br.com.api.desafio.Services;

import br.com.api.desafio.Dtos.ModuleResponseDTO;
import br.com.api.desafio.Repository.ModuleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ModuleService {
    private final ModuleRepository moduleRepository;

    public List<ModuleResponseDTO> listAll() {
        return moduleRepository.findAll()
                .stream()
                .map(ModuleResponseDTO::fromEntity)
                .toList();
    }
}

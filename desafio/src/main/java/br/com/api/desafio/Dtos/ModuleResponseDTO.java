package br.com.api.desafio.Dtos;

import br.com.api.desafio.Model.Modules;

import java.util.Set;
import java.util.stream.Collectors;

public record ModuleResponseDTO(
        String name,
        String description,
        boolean active,
        Set<String> allowedDepartments,
        Set<String> incompatibleModules
) {
    public static ModuleResponseDTO fromEntity(Modules m) {
        return new ModuleResponseDTO(
                m.getName(),
                m.getDescription(),
                m.isActive(),
                m.getAllowedDepartments()
                        .stream()
                        .map(Enum::name)
                        .collect(Collectors.toSet()),
                m.getIncompatibleWith()
                        .stream()
                        .map(Modules::getName)
                        .collect(Collectors.toSet())
        );
    }
}

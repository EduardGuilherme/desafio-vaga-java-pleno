package br.com.api.desafio.Dtos;

import java.util.Set;
import java.util.UUID;

public record CreateAccessRequestDTO(
        Set<UUID> moduleIds,
        String justification,
        boolean urgent
) {
}

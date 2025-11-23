package br.com.api.desafio.Dtos;

import br.com.api.desafio.Model.AccessRequest;
import br.com.api.desafio.Model.Modules;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record ResponseAccessRequestDTO(
        UUID id,
        String protocol,
        String requesterName,
        Set<String> modules,
        String justification,
        boolean urgent,
        String status,
        LocalDateTime createdAt,
        LocalDateTime expiresAt,
        String denialReason
) {
    public static ResponseAccessRequestDTO fromEntity(AccessRequest req) {
        return new ResponseAccessRequestDTO(
                req.getId(),
                req.getProtocol(),
                req.getRequester().getName(),
                req.getModules().stream().map(Modules::getName).collect(java.util.stream.Collectors.toSet()),
                req.getJustification(),
                req.isUrgent(),
                req.getStatus().name(),
                req.getCreatedAt(),
                req.getExpiresAt(),
                req.getDenialReason()
        );
    }
}

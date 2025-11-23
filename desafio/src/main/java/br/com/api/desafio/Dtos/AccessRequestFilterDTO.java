package br.com.api.desafio.Dtos;

import br.com.api.desafio.Enums.RequestStatus;

import java.time.LocalDateTime;

public record AccessRequestFilterDTO(
        String text,
        RequestStatus status,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Boolean urgent
) {
}

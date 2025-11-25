package br.com.api.desafio.Controller;

import br.com.api.desafio.Dtos.*;
import br.com.api.desafio.Enums.RequestStatus;
import br.com.api.desafio.Services.AccessRequestService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/requests")
@SecurityRequirement(name = "bearerAuth")
public class AccessRequestController {

    private final AccessRequestService service;

    @PostMapping
    public ResponseEntity<ResponseAccessRequestDTO> create(
            Principal principal,
            @Valid @RequestBody CreateAccessRequestDTO dto) {

        UUID userId = UUID.fromString(principal.getName());
        var req = service.createRequest(dto, userId);
        return ResponseEntity.ok(ResponseAccessRequestDTO.fromEntity(req));
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<ResponseAccessRequestDTO> cancel(
            @PathVariable UUID requestId,
            @Valid @RequestBody CancelRequestDTO dto,
            Principal principal) {

        UUID userId = UUID.fromString(principal.getName());
        var req = service.cancelRequest(requestId, userId, dto.reason());
        return ResponseEntity.ok(ResponseAccessRequestDTO.fromEntity(req));
    }

    @PatchMapping("/{requestId}/renew")
    public ResponseEntity<ResponseAccessRequestDTO> renew(
            @PathVariable UUID requestId,
            Principal principal) {

        UUID userId = UUID.fromString(principal.getName());
        var req = service.renewRequest(requestId, userId);
        return ResponseEntity.ok(ResponseAccessRequestDTO.fromEntity(req));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseAccessRequestDTO> getById(@PathVariable UUID id) {
        var req = service.getById(id);
        return ResponseEntity.ok(ResponseAccessRequestDTO.fromEntity(req));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ResponseAccessRequestDTO>> getByUser(@PathVariable UUID userId) {
        var list = service.getByUser(userId)
                .stream()
                .map(ResponseAccessRequestDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ResponseAccessRequestDTO>> search(
            Principal principal,
            @RequestParam(required = false) String text,
            @RequestParam(required = false) RequestStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end,
            @RequestParam(required = false) Boolean urgent,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        UUID userId = UUID.fromString(principal.getName());

        AccessRequestFilterDTO filters = new AccessRequestFilterDTO(text, status, start, end, urgent);

        var result = service.searchRequests(userId, filters, page, size)
                .map(ResponseAccessRequestDTO::fromEntity);

        return ResponseEntity.ok(result);
    }


}

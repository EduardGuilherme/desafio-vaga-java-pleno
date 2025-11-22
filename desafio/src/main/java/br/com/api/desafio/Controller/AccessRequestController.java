package br.com.api.desafio.Controller;

import br.com.api.desafio.Dtos.CreateAccessRequestDTO;
import br.com.api.desafio.Model.AccessRequest;
import br.com.api.desafio.Services.AccessRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/requests")
public class AccessRequestController {

    private final AccessRequestService service;

    @PostMapping("/{id}")
    public ResponseEntity<AccessRequest> createRequest(
            @PathVariable UUID id,
            @RequestBody CreateAccessRequestDTO dto){
        return ResponseEntity.ok(service.createRequest(dto,id));

    }
}

package br.com.api.desafio.Controller;

import br.com.api.desafio.Dtos.ModuleResponseDTO;
import br.com.api.desafio.Services.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/modules")
@RequiredArgsConstructor
public class ModuleController {

    private final ModuleService moduleService;

    @GetMapping
    public ResponseEntity<List<ModuleResponseDTO>> listAll() {
        return ResponseEntity.ok(moduleService.listAll());
    }
}

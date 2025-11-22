package br.com.api.desafio.Services;

import br.com.api.desafio.Dtos.CreateAccessRequestDTO;
import br.com.api.desafio.Enums.Departament;
import br.com.api.desafio.Enums.DepartmentRules;
import br.com.api.desafio.Enums.RequestStatus;
import br.com.api.desafio.Model.AccessRequest;
import br.com.api.desafio.Model.User;
import br.com.api.desafio.Model.Modules;
import br.com.api.desafio.Repository.AccessRequestRepository;
import br.com.api.desafio.Repository.ModuleRepository;
import br.com.api.desafio.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccessRequestService {
    private final AccessRequestRepository accessRequestRepository;
    private final ModuleRepository moduleRepository;
    private final UserRepository userRepository;

    public AccessRequest createRequest(CreateAccessRequestDTO dto, UUID userId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // 1 — Validar quantidade de módulos
        if (dto.moduleIds().isEmpty() || dto.moduleIds().size() > 3) {
            throw new IllegalArgumentException("Selecione entre 1 e 3 módulos.");
        }

        // 2 — Buscar módulos no banco
        Set<Modules> modules = new HashSet<>(
                moduleRepository.findAllById(dto.moduleIds())
        );
        if (modules.size() != dto.moduleIds().size()) {
            throw new IllegalArgumentException("Um ou mais módulos não existem.");
        }

        // 3 — Validar justificativa
        validateJustification(dto.justification());

        // 4 — Validar se módulos estão ativos
        modules.forEach(m -> {
            if (!m.isActive()) {
                throw new IllegalArgumentException("Módulo " + m.getName() + " está inativo.");
            }
        });

        // 5 — Validar departamento
        modules.forEach(m -> {
            if (!m.getAllowedDepartments().contains(user.getDepartment())) {
                throw new IllegalArgumentException("Departamento sem permissão para acessar " + m.getName());
            }
        });

        // 6 — Validar incompatibilidade entre módulos
        validateModuleCompatibility(modules, user);

        // 7 — Validar limite de módulos do usuário
        validateUserModuleLimit(user, modules.size());

        // 8 — Validar solicitações duplicadas
        if (accessRequestRepository.existsByRequesterIdAndModulesIdInAndStatus(
                user.getId(), dto.moduleIds(), RequestStatus.ATIVO)) {
            throw new IllegalArgumentException("Já existe solicitação ativa para um dos módulos.");
        }

        // Criar solicitação
        AccessRequest request = AccessRequest.builder()
                .id(null)
                .requester(user)
                .modules(modules)
                .justification(dto.justification())
                .urgent(dto.urgent())
                .createdAt(LocalDateTime.now())
                .protocol(generateProtocol())
                .build();

        // Verificar regras e ativar ou negar
        applyBusinessRules(request, user);

        return accessRequestRepository.save(request);
    }

    private String generateProtocol() {
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        int number = (int) (Math.random() * 9000) + 1000;
        return "SOL-" + date + "-" + number;
    }

    private void validateJustification(String text) {
        if (text == null || text.length() < 20 || text.length() > 500) {
            throw new IllegalArgumentException("A justificativa deve ter entre 20 e 500 caracteres.");
        }

        List<String> invalid = List.of("teste", "aaa", "preciso", "aaaa", "nao sei");

        if (invalid.contains(text.toLowerCase().trim())) {
            throw new IllegalArgumentException("Justificativa insuficiente ou genérica.");
        }
    }

    private void validateModuleCompatibility(Set<Modules> modules, User user) {

        // Regras de incompatibilidade globais (entre módulos ativos do usuário)
        Set<String> activeModules = user.getActiveModules(); // você adicionará depois no perfil de usuário

        for (Modules current : modules) {
            // Verificar incompatibilidade com módulos já ativos
            for (String active : activeModules) {
                if (current.getIncompatibleWith().stream()
                        .anyMatch(m -> m.getName().equalsIgnoreCase(active))) {
                    throw new IllegalArgumentException(
                            "Módulo incompatível com outro módulo já ativo em seu perfil");
                }
            }

            // Verificar incompatibilidade entre módulos da própria solicitação
            for (Modules other : modules) {
                if (!current.equals(other) && current.getIncompatibleWith().contains(other)) {
                    throw new IllegalArgumentException(
                            "Módulos incompatíveis selecionados: "
                                    + current.getName() + " e " + other.getName()
                    );
                }
            }
        }
    }

    private void validateUserModuleLimit(User user, int requestedModules) {

        int maxModules = (user.getDepartment() == Departament.TI) ? 10 : 5;

        int activeCount = user.getActiveModules().size();

        if (activeCount + requestedModules > maxModules) {
            throw new IllegalArgumentException("Limite de módulos ativos atingido");
        }
    }

    private void applyBusinessRules(AccessRequest request, User user) {

        try {
            // Regra 1: Departamento pode acessar módulos selecionados?
            for (Modules m : request.getModules()) {
                if (!DepartmentRules.getAllowedModules(user.getDepartment())
                        .contains(m.getName())) {
                    deny(request, "Departamento sem permissão para acessar este módulo");
                    return;
                }
            }

            // Regra 2: Incompatibilidades
            for (Modules m : request.getModules()) {
                for (Modules incompatible : m.getIncompatibleWith()) {
                    if (request.getModules().contains(incompatible)) {
                        deny(request, "Módulo incompatível com outro módulo solicitado");
                        return;
                    }
                }
            }

            // Regra 3: Limite de módulos
            int active = user.getActiveModules().size();
            int limit = (user.getDepartment() == Departament.TI) ? 10 : 5;

            if (active + request.getModules().size() > limit) {
                deny(request, "Limite de módulos ativos atingido");
                return;
            }

            // Se chegou aqui, está aprovado
            approve(request, user);

        } catch (Exception e) {
            deny(request, e.getMessage());
        }
    }

    private void approve(AccessRequest request, User user) {

        request.setStatus(RequestStatus.ATIVO);
        request.setDenialReason(null);

        // Expira em 180 dias
        request.setExpiresAt(LocalDateTime.now().plusDays(180));

        // Atualizar módulos ativos do usuário
        user.getActiveModules().addAll(
                request.getModules().stream().map(Modules::getName).toList()
        );

        userRepository.save(user);
    }

    private void deny(AccessRequest request, String reason) {
        request.setStatus(RequestStatus.NEGADO);
        request.setDenialReason(reason);
        request.setExpiresAt(null);
    }

}

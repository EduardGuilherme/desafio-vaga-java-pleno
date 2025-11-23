package br.com.api.desafio.Specifications;

import br.com.api.desafio.Dtos.AccessRequestFilterDTO;
import br.com.api.desafio.Model.AccessRequest;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.UUID;


public class AccessRequestSpecs {
    public static Specification<AccessRequest> filter(UUID userId, AccessRequestFilterDTO f) {
        return (root, query, cb) -> {
            Predicate predicate = cb.conjunction();

            // Filtrar apenas solicitações do usuário:
            predicate = cb.and(predicate, cb.equal(root.get("requester").get("id"), userId));

            // Texto: protocolo ou nome do módulo
            if (f.text() != null && !f.text().isBlank()) {
                String like = "%" + f.text().toLowerCase() + "%";

                Join<Object, Object> modules = root.join("modules", JoinType.LEFT);

                Predicate protocolMatch = cb.like(cb.lower(root.get("protocol")), like);
                Predicate moduleMatch = cb.like(cb.lower(modules.get("name")), like);

                predicate = cb.and(predicate, cb.or(protocolMatch, moduleMatch));
            }

            // Status
            if (f.status() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("status"), f.status()));
            }

            // Período
            if (f.startDate() != null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(
                        root.get("createdAt"), f.startDate()
                ));
            }

            if (f.endDate() != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(
                        root.get("createdAt"), f.endDate()
                ));
            }

            // Urgência
            if (f.urgent() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("urgent"), f.urgent()));
            }

            return predicate;
        };
    }
}

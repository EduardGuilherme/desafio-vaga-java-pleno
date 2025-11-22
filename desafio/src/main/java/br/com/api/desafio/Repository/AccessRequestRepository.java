package br.com.api.desafio.Repository;

import br.com.api.desafio.Enums.RequestStatus;
import br.com.api.desafio.Model.AccessRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Set;
import java.util.UUID;

public interface AccessRequestRepository extends JpaRepository<AccessRequest, UUID> {
    boolean existsByRequesterIdAndModulesIdInAndStatus(
            UUID userId, Set<UUID> moduleIds, RequestStatus status
    );
}

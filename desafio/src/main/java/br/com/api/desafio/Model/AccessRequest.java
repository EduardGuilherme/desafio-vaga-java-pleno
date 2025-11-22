package br.com.api.desafio.Model;

import br.com.api.desafio.Enums.RequestStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Entity
@Table
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class AccessRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String protocol;

    @ManyToOne
    private User requester;

    @ManyToMany
    private Set<Modules> modules;

    private String justification;

    private boolean urgent;

    @Enumerated(EnumType.STRING)
    private RequestStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    private String denialReason;
}

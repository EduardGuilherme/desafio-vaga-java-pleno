package br.com.api.desafio.Model;

import br.com.api.desafio.Enums.Departament;
import br.com.api.desafio.Enums.DepartamentConverter;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Data
@Entity
@Builder
@Table(name = "users")
@Getter
@Setter
@RequiredArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    @Convert(converter = DepartamentConverter.class)
    private Departament department;

    @ElementCollection(fetch = FetchType.EAGER)
    private Set<String> activeModules = new HashSet<>();
}

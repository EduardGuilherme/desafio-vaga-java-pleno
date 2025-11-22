package br.com.api.desafio.Repository;

import br.com.api.desafio.Model.Modules;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ModuleRepository extends JpaRepository<Modules, UUID> {
}

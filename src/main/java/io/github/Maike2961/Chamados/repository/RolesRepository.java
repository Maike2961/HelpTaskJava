package io.github.Maike2961.Chamados.repository;

import io.github.Maike2961.Chamados.models.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RolesRepository extends JpaRepository<Roles, Long> {

    Optional<Roles> findByDescricao(String name);
}

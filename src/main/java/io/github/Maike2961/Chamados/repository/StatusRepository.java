package io.github.Maike2961.Chamados.repository;

import io.github.Maike2961.Chamados.models.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Long> {
}

package io.github.Maike2961.Chamados.repository;

import io.github.Maike2961.Chamados.models.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.util.UUID;

public interface UsersRepository extends JpaRepository<Users, UUID> {

    Optional<Users> findByUsername(String username);


}

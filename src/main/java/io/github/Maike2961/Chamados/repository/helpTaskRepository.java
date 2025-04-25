package io.github.Maike2961.Chamados.repository;

import io.github.Maike2961.Chamados.models.Users;
import io.github.Maike2961.Chamados.models.helpTask;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface helpTaskRepository extends JpaRepository<helpTask, Long> {

    List<helpTask> findByUsers(Users role);

}

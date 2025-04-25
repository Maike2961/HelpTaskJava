package io.github.Maike2961.Chamados.service;

import io.github.Maike2961.Chamados.repository.UsersRepository;
import io.github.Maike2961.Chamados.repository.helpTaskRepository;
import io.github.Maike2961.Chamados.controller.dto.taskRequestDTO;
import io.github.Maike2961.Chamados.controller.dto.taskResponseDTO;
import io.github.Maike2961.Chamados.controller.dto.updateRequestDTO;
import io.github.Maike2961.Chamados.exception.IdNaoEncontrado;
import io.github.Maike2961.Chamados.exception.UsernameNaoEncontrado;
import io.github.Maike2961.Chamados.models.Users;
import io.github.Maike2961.Chamados.models.helpTask;
import io.github.Maike2961.Chamados.service.AWS.awsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class helpTaskService {

    @Autowired
    private helpTaskRepository helpTaskRespository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private awsService awsService;

    public List<helpTask> listAllTasks() {
        return helpTaskRespository.findAll()
                .stream()
                .filter(task -> !task.getStatus().getDescricao().equals("concluido")).toList();
    }

    public List<taskResponseDTO> listUserOrAdminTasks() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Users usuarioEncontrado = usersRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UsernameNaoEncontrado("Usuário não encontrado"));

        if (hasRole(usuarioEncontrado, "user") || hasRole(usuarioEncontrado, "admin")) {
            return helpTaskRespository.findByUsers(usuarioEncontrado).stream()
                    .map(dto -> new taskResponseDTO(
                            dto.getHelpTitle(),
                            dto.getDescricao(),
                            dto.getUsers().getUsername(),
                            dto.getStatus()
                    )).toList();
        }
        return null;
    }

    public taskResponseDTO updateTask(Long id, updateRequestDTO novoStatus) {

        return helpTaskRespository.findById(id)
                .map(task -> {
                    task.setStatus(novoStatus.status().toStatus());
                    helpTaskRespository.save(task);

                    awsService.publishChamadoConcluido(novoStatus.status(), task.getHelpTitle());

                    return new taskResponseDTO(
                            task.getHelpTitle(),
                            task.getDescricao(),
                            task.getUsers().getUsername(),
                            task.getStatus()
                    );
                })
                .orElseThrow(() -> new IdNaoEncontrado("ID do chamado não encontrado"));
    }

    public helpTask createTask(taskRequestDTO dto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Users usuarioCadastrado = usersRepository.findByUsername(authentication.getName())
                .orElseThrow(() -> new UsernameNaoEncontrado("Usuário não cadastrado"));

        helpTask entity = dto.toEntity();
        entity.setUsers(usuarioCadastrado);

        return helpTaskRespository.save(entity);

    }

    public boolean hasRole(Users user, String roleName) {
        return user.getRoles().stream()
                .anyMatch(role -> role.getDescricao().equalsIgnoreCase(roleName));
    }
}

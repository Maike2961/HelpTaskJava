package io.github.Maike2961.Chamados.controller;

import io.github.Maike2961.Chamados.Enums.StatusEnum;
import io.github.Maike2961.Chamados.controller.dto.taskRequestDTO;
import io.github.Maike2961.Chamados.controller.dto.taskResponseDTO;
import io.github.Maike2961.Chamados.controller.dto.updateRequestDTO;
import io.github.Maike2961.Chamados.models.helpTask;
import io.github.Maike2961.Chamados.service.helpTaskService;
import jakarta.validation.Valid;
import org.hibernate.sql.Update;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("v1/task")
public class helpTaskController {

    @Autowired
    private helpTaskService service;

    @GetMapping("tecnical")
    @PreAuthorize("hasRole('tecnical')")
    public ResponseEntity<?> listAllTasksToTecnical(){
        List<taskResponseDTO> collect = service.listAllTasks().stream().map(dto
                -> new taskResponseDTO(
                        dto.getHelpTitle(),
                        dto.getDescricao(),
                        dto.getUsers().getUsername(),
                        dto.getStatus())).collect(Collectors.toList());
        return ResponseEntity.ok().body(collect);
    }

    @GetMapping("my-own-tasks")
    @PreAuthorize("hasAnyRole('admin', 'user')")
    public ResponseEntity<?> listAdminOrUserTasks(){
        List<taskResponseDTO> taskResponseDTOS = service.listUserOrAdminTasks();
        if(taskResponseDTOS == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND.value()).build();
        }
        return ResponseEntity.ok().body(taskResponseDTOS);
    }


    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('admin', 'user')")
    public ResponseEntity<?> createHelpTask(@RequestBody @Valid taskRequestDTO dto){
        helpTask task = service.createTask(dto);
        return ResponseEntity.status(HttpStatus.CREATED.value()).body(new taskResponseDTO(
                task.getHelpTitle(),
                task.getDescricao(),
                task.getUsers().getUsername(),
                task.getStatus()));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('tecnical')")
    public ResponseEntity<?> updateTask(
            @PathVariable Long id,
            @RequestBody @Valid updateRequestDTO status
            ){
        taskResponseDTO taskResponseDTO = service.updateTask(id, status);

        return ResponseEntity.ok().body(taskResponseDTO);
    }
}

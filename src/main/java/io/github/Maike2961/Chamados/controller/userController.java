package io.github.Maike2961.Chamados.controller;


import io.github.Maike2961.Chamados.controller.dto.userRequestDTO;
import io.github.Maike2961.Chamados.controller.dto.userResponseDTO;
import io.github.Maike2961.Chamados.models.Users;
import io.github.Maike2961.Chamados.service.userService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/user")
public class userController {

    @Autowired
    private userService service;

    @PostMapping("/create")
    public ResponseEntity<?> salvarUser(@RequestBody @Valid userRequestDTO dto) {
        Users user = service.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED.value())
                .body(new userResponseDTO(
                        user.getUsername(),
                        user.getRoles(),
                        user.getDataCadastro(),
                        user.getDataAtualizacao()));
    }
}

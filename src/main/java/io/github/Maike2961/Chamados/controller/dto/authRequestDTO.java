package io.github.Maike2961.Chamados.controller.dto;

import jakarta.validation.constraints.NotBlank;

public record authRequestDTO(
        @NotBlank(message = "username é obrigatório")
        String username,
        @NotBlank(message = "password é obrigatório")
        String password) {
}

package io.github.Maike2961.Chamados.controller.dto;

import io.github.Maike2961.Chamados.models.Roles;
import java.time.LocalDateTime;
import java.util.Set;

public record userResponseDTO(String username,
                              Set<Roles> roles,
                              LocalDateTime dataCadastro,
                              LocalDateTime dataAtualizado) {
}

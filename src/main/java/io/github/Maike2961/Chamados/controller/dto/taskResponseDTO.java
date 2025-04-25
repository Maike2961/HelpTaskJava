package io.github.Maike2961.Chamados.controller.dto;

import io.github.Maike2961.Chamados.models.Status;

public record taskResponseDTO(String title,
                              String descricao,
                              String nomeCriador,
                              Status status) {
}

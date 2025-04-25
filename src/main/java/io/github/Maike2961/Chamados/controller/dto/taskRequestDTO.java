package io.github.Maike2961.Chamados.controller.dto;

import io.github.Maike2961.Chamados.Enums.StatusEnum;
import io.github.Maike2961.Chamados.models.helpTask;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record taskRequestDTO(
        @NotBlank(message = "helpTile é obrigatório")
        String helpTitle,
        @NotNull(message = "status é obrigatório")
        String descricao) {

    public helpTask toEntity() {
        return new helpTask(
                helpTitle,
                descricao,
                StatusEnum.PENDENTE.toStatus());
    }
}

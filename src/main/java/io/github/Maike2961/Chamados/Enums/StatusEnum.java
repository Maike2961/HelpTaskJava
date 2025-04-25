package io.github.Maike2961.Chamados.Enums;

import io.github.Maike2961.Chamados.models.Status;
import jakarta.persistence.Id;

public enum StatusEnum {
    PENDENTE(1L, "pendente"),
    ERRO(2L, "erro"),
    CANCELADO(3L, "cancelado"),
    CONCLUIDO(4L, "concluido");

    private Long id;
    private String descricao;

    StatusEnum(Long id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public Status toStatus(){
        return new Status(this.id, this.descricao);
    }

}

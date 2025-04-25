package io.github.Maike2961.Chamados.models;

import jakarta.persistence.*;

@Entity
@Table(name = "tb_status")
public class Status {

    @Id
    private Long id;

    private String descricao;

    public Status(Long id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    public Status() {
    }

}

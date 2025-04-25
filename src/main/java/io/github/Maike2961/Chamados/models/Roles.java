package io.github.Maike2961.Chamados.models;

import io.github.Maike2961.Chamados.Enums.RolesEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "tb_roles")
public class Roles {
    @Id
    private Long id;

    private String descricao;

    public Roles(Long id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public Roles() {
    }

    public Roles(RolesEnum rolesEnum) {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}

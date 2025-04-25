package io.github.Maike2961.Chamados.Enums;

import io.github.Maike2961.Chamados.models.Roles;

public enum RolesEnum {
    USER(1L, "user"),
    TECNICAL(2L, "tecnical"),
    ADMIN(3L, "admin");

    private Long id;
    private String name;

    public String getName() {
        return name;
    }

    RolesEnum(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Roles toRole() {
        return new Roles(this.id, this.name);
    }
}

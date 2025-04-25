package io.github.Maike2961.Chamados.controller.dto;

import io.github.Maike2961.Chamados.Enums.RolesEnum;
import io.github.Maike2961.Chamados.models.Roles;
import io.github.Maike2961.Chamados.models.Users;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

public record userRequestDTO(

        @NotBlank(message = "Campo obrigatório")
        String username,
        @NotBlank(message = "Campo obrigatório")
        String password,
        @NotNull(message = "Role é obrigatório")
        Set<RolesEnum> rolesEnum) {

    public Users toEntity(Set<Roles> roles){
        return new Users(
                username,
                password,
                roles
        );
    }
}

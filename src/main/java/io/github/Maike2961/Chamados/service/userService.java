package io.github.Maike2961.Chamados.service;

import io.github.Maike2961.Chamados.repository.RolesRepository;
import io.github.Maike2961.Chamados.repository.UsersRepository;
import io.github.Maike2961.Chamados.controller.dto.userRequestDTO;
import io.github.Maike2961.Chamados.models.Roles;
import io.github.Maike2961.Chamados.models.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class userService {

    @Autowired
    private RolesRepository rolesRepository;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Users createUser(userRequestDTO dto){
        Set<Roles> rolesEnums = dto.rolesEnum().stream().map(e ->
                        rolesRepository.findByDescricao(e.getName())
                                .orElseThrow(() -> new RuntimeException("Role não encontrada")))
                .collect(Collectors.toSet());

        Users users = dto.toEntity(rolesEnums);
        users.setPassword(passwordEncoder.encode(dto.password()));
        return usersRepository.save(users);
    }


}

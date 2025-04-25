package io.github.Maike2961.Chamados.config;

import io.github.Maike2961.Chamados.Enums.RolesEnum;
import io.github.Maike2961.Chamados.Enums.StatusEnum;
import io.github.Maike2961.Chamados.repository.RolesRepository;
import io.github.Maike2961.Chamados.repository.StatusRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class ConfigLoader implements CommandLineRunner {

    @Autowired
    private StatusRepository statusRepository;

    @Autowired
    private RolesRepository rolesRepository;


    @Override
    public void run(String... args) throws Exception {
        Arrays.stream(StatusEnum.values()).map(StatusEnum::toStatus).forEach(statusRepository::save);
        Arrays.stream(RolesEnum.values()).map(RolesEnum::toRole).forEach(rolesRepository::save);

    }
}

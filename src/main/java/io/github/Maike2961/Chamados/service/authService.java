package io.github.Maike2961.Chamados.service;

import io.github.Maike2961.Chamados.repository.UsersRepository;
import io.github.Maike2961.Chamados.controller.dto.authRequestDTO;
import io.github.Maike2961.Chamados.exception.UsernameNaoEncontrado;
import io.github.Maike2961.Chamados.exception.senhaIncorreta;
import io.github.Maike2961.Chamados.models.Users;
import io.github.Maike2961.Chamados.service.token.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class authService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private Token token;

    public String autenticar(authRequestDTO dto) {

        Users usuarioEncontrado = usersRepository
                .findByUsername(dto.username()).orElseThrow(()
                        -> new UsernameNaoEncontrado("Usuário não encontrado"));

        boolean matches = passwordEncoder.matches(dto.password(), usuarioEncontrado.getPassword());

        if (!matches) {
            throw new senhaIncorreta("Senha inválida");
        }
        return token.obterToken(dto);

    }

}

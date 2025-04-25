package io.github.Maike2961.Chamados.config;


import io.github.Maike2961.Chamados.repository.UsersRepository;
import io.github.Maike2961.Chamados.exception.UsernameNaoEncontrado;
import io.github.Maike2961.Chamados.models.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UsersRepository repository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Users usuarioEncontrado = repository.findByUsername(username)
                .orElseThrow(() -> new UsernameNaoEncontrado("Usuário não encontrado"));

        Set<SimpleGrantedAuthority> authorities = usuarioEncontrado.getRoles().stream().map(role ->
                new SimpleGrantedAuthority("ROLE_" +role.getDescricao())).collect(Collectors.toSet());

        return User
                .builder()
                .username(usuarioEncontrado.getUsername())
                .password(usuarioEncontrado.getPassword())
                .authorities(authorities)
                .build();
    }
}

package io.github.Maike2961.Chamados.exception;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UsernameNaoEncontrado extends UsernameNotFoundException {
    public UsernameNaoEncontrado(String mensagem){
        super(mensagem);
    }
}

package io.github.Maike2961.Chamados.exception;

import org.springframework.security.authentication.BadCredentialsException;

public class senhaIncorreta extends BadCredentialsException {
    public senhaIncorreta(String message) {
        super(message);
    }
}

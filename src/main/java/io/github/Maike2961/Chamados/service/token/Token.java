package io.github.Maike2961.Chamados.service.token;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import io.github.Maike2961.Chamados.repository.UsersRepository;
import io.github.Maike2961.Chamados.controller.dto.authRequestDTO;
import io.github.Maike2961.Chamados.exception.UsernameNaoEncontrado;
import io.github.Maike2961.Chamados.exception.TokenInvalido;
import io.github.Maike2961.Chamados.models.Roles;
import io.github.Maike2961.Chamados.models.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Optional;

@Component
public class Token {

    @Autowired
    private UsersRepository repository;


    public String obterToken(authRequestDTO dto){

        Optional<Users> byUsername = repository.findByUsername(dto.username());
        if(byUsername.isPresent()){
            Users user = byUsername.get();
            System.out.println("Username: " + user.getUsername());
            System.out.println("Roles: " + user.getRoles().stream().map(Roles::getDescricao).toList());
            return gerarTokenJWT(byUsername.get());
        }
        throw new UsernameNaoEncontrado("Usuário não encontrado");
    }


    public String validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("My-secret");

            return JWT.require(algorithm)
                    .withIssuer("auten")
                    .build()
                    .verify(token)
                    .getSubject();

        } catch (JWTVerificationException e) {
            throw new TokenInvalido("Token inválido ou expirado");
        }
    }

    public String gerarTokenJWT(Users user) {
        try {
            Algorithm algorithm = Algorithm.HMAC256("My-secret");

            return JWT.create()
                    .withIssuer("auten")
                    .withSubject(user.getUsername())
                    .withExpiresAt(gerarExpiracao())
                    .sign(algorithm);
        } catch (JWTVerificationException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private Instant gerarExpiracao() {
        return LocalDateTime.now().plusHours(8).toInstant(ZoneOffset.of("-03:00"));
    }
}

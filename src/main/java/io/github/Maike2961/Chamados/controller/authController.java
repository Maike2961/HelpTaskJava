package io.github.Maike2961.Chamados.controller;

import io.github.Maike2961.Chamados.controller.dto.authRequestDTO;
import io.github.Maike2961.Chamados.service.authService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("v1/user")
public class authController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private authService service;


    @GetMapping("/admin")
    public String heyAdmin() {
        return "Olá admin";
    }

    @GetMapping
    public String heyUser() {
        return "Olá user";
    }

    @GetMapping("/tecnical")
    public String heyTecnical() {
        return "Olá tecnical";
    }

    @PostMapping("/auth")
    public ResponseEntity<?> authLogin(@RequestBody @Valid authRequestDTO dto) {
        var auth = new UsernamePasswordAuthenticationToken(dto.username(), dto.password());

        String autenticar = service.autenticar(dto);
        if(autenticar != null){
            authenticationManager.authenticate(auth);
            return ResponseEntity.ok().body(autenticar);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Autenticação falhou");
    }
}

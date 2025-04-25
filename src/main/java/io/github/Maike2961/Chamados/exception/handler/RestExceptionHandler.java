package io.github.Maike2961.Chamados.exception.handler;

import io.github.Maike2961.Chamados.exception.IdNaoEncontrado;
import io.github.Maike2961.Chamados.exception.TokenInvalido;
import io.github.Maike2961.Chamados.exception.UsernameNaoEncontrado;
import io.github.Maike2961.Chamados.exception.dto.ErroCampoValidacao;
import io.github.Maike2961.Chamados.exception.senhaIncorreta;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<?> handlerCampoInvalido(MethodArgumentNotValidException e) {

        List<FieldError> fieldErrors = e.getFieldErrors();

        List<ErroCampoValidacao> erroDeValidacao = fieldErrors.stream().map(err ->
                new ErroCampoValidacao("Erro de validação",
                        List.of(
                                err.getField(),
                                err.getDefaultMessage()
                        ))).collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY.value()).body(erroDeValidacao);
    }


    @ExceptionHandler(UsernameNaoEncontrado.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<?> handlerUsernameNoFound(UsernameNaoEncontrado e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value())
                .body(new ErroCampoValidacao("Usuário não encontrado", List.of(e.getMessage(), e.getLocalizedMessage())));
    }

    @ExceptionHandler(senhaIncorreta.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<?> handlerSenhaIncorreta(senhaIncorreta e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED.value())
                .body(new ErroCampoValidacao("Senha é incorreta", List.of(e.getMessage(), e.getLocalizedMessage())));
    }

    @ExceptionHandler(IdNaoEncontrado.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    public ResponseEntity<?> hanlderIdNaoEncontrado(IdNaoEncontrado e){
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY.value())
                .body(new ErroCampoValidacao("Id não encontrado", List.of(e.getMessage(), e.getCause().toString())));
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<?> handlerErroInterno(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR.value()).body(new ErroCampoValidacao("Erro interno",
                List.of(e.getMessage(), e.getLocalizedMessage())));
    }

}

package io.github.Maike2961.Chamados.exception.dto;

import java.util.List;

public record ErroCampoValidacao(String title, List<String> message) {

}

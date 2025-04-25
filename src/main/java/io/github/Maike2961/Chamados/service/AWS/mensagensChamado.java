package io.github.Maike2961.Chamados.service.AWS;

import io.github.Maike2961.Chamados.Enums.StatusEnum;

import java.util.HashMap;
import java.util.Map;

public class mensagensChamado {

    private static final Map<StatusEnum, String> mensagens = new HashMap<>();

    static {
        mensagens.put(StatusEnum.CONCLUIDO, "O chamado '%s' foi concluído com sucesso!");
        mensagens.put(StatusEnum.CANCELADO, "O chamado '%s' foi cancelado.");
        mensagens.put(StatusEnum.ERRO, "O chamado '%s' apresentou um erro.");
    }

    public static String getMensagem(StatusEnum status, String titulo){
        return String.format(
                mensagens.getOrDefault(status, "Status desconhecido"),
                titulo
        );
    }
}

package com.matheuszanatta.desafiovotacao.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PautaRequest {

    @NotBlank(message = "O título não pode ser vazio")
    private String titulo;

    @NotBlank(message = "A descrição não pode ser vazia")
    private String descricao;
}

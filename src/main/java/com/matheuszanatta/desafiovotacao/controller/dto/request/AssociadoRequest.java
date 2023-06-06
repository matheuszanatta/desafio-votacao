package com.matheuszanatta.desafiovotacao.controller.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.br.CPF;

@Getter
@Setter
public class AssociadoRequest {

    @NotBlank(message = "O nome do associado não pode ser vazio")
    private String nome;

    @NotBlank(message = "O CPF do associado não pode ser vazio")
    @CPF(message = "O CPF do associado deve ser válido")
    private String cpf;
}

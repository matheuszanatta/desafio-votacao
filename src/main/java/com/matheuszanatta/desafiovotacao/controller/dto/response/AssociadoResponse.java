package com.matheuszanatta.desafiovotacao.controller.dto.response;

import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
public class AssociadoResponse {

    private Long id;
    private String nome;
    private String cpf;
}

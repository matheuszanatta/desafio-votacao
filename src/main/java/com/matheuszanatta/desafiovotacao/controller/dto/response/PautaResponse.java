package com.matheuszanatta.desafiovotacao.controller.dto.response;

import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
public class PautaResponse {

    private Long id;
    private String titulo;
    private String descricao;
}

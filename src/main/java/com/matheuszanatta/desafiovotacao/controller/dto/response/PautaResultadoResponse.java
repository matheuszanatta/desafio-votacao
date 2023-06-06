package com.matheuszanatta.desafiovotacao.controller.dto.response;

import com.matheuszanatta.desafiovotacao.domain.enums.ResultadoVoto;
import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
public class PautaResultadoResponse {

    private Long id;
    private int votos;
    private int votosSim;
    private int votosNao;
    private ResultadoVoto resultado;
}

package com.matheuszanatta.desafiovotacao.service.pauta;

import com.matheuszanatta.desafiovotacao.domain.enums.ResultadoVoto;
import lombok.*;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
public class MensagemPautaEncerrada {

    private Long id;
    private ResultadoVoto resultado;
}

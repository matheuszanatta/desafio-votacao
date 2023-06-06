package com.matheuszanatta.desafiovotacao.controller.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
@NoArgsConstructor
@Setter
public class SessaoResponse {

    private Long id;
    private LocalDateTime dataInicial;
    private LocalDateTime dataFinal;
}

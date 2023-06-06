package com.matheuszanatta.desafiovotacao.mapper;

import com.matheuszanatta.desafiovotacao.controller.dto.response.SessaoResponse;
import com.matheuszanatta.desafiovotacao.domain.Sessao;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class SessaoMapper {

    public static SessaoResponse toResponse(Sessao entity) {
        return SessaoResponse.builder()
                .id(entity.getId())
                .dataInicial(entity.getDataInicial())
                .dataFinal(entity.getDataFinal())
                .build();
    }
}

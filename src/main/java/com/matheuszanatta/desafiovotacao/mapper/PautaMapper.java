package com.matheuszanatta.desafiovotacao.mapper;

import com.matheuszanatta.desafiovotacao.controller.dto.request.PautaRequest;
import com.matheuszanatta.desafiovotacao.controller.dto.response.PautaResponse;
import com.matheuszanatta.desafiovotacao.domain.Pauta;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class PautaMapper {

    public static Pauta toEntity(PautaRequest request) {
        return Pauta.builder()
                .titulo(request.getTitulo())
                .descricao(request.getDescricao())
                .build();
    }

    public static PautaResponse toResponse(Pauta entity) {
        return PautaResponse.builder()
                .id(entity.getId())
                .titulo(entity.getTitulo())
                .descricao(entity.getDescricao())
                .build();
    }
}

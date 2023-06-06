package com.matheuszanatta.desafiovotacao.mapper;

import com.matheuszanatta.desafiovotacao.controller.dto.request.AssociadoRequest;
import com.matheuszanatta.desafiovotacao.controller.dto.response.AssociadoResponse;
import com.matheuszanatta.desafiovotacao.domain.Associado;
import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
public class AssociadoMapper {

    public static Associado toEntity(AssociadoRequest request) {
        return Associado.builder()
                .nome(request.getNome())
                .cpf(request.getCpf())
                .build();
    }

    public static AssociadoResponse toResponse(Associado entity) {
        return AssociadoResponse.builder()
                .id(entity.getId())
                .nome(entity.getNome())
                .cpf(entity.getCpf())
                .build();
    }
}

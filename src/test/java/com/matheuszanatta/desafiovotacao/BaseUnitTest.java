package com.matheuszanatta.desafiovotacao;

import com.matheuszanatta.desafiovotacao.controller.dto.request.VotoRequest;
import com.matheuszanatta.desafiovotacao.domain.Associado;
import com.matheuszanatta.desafiovotacao.domain.Pauta;
import com.matheuszanatta.desafiovotacao.domain.Sessao;
import com.matheuszanatta.desafiovotacao.domain.enums.VotoComputado;
import com.matheuszanatta.desafiovotacao.repository.projection.VotoResultado;

import static java.time.LocalDateTime.now;

public abstract class BaseUnitTest {

    protected Pauta buildPautaEntity(Long pautaId) {
        return Pauta.builder()
                .id(pautaId)
                .build();
    }

    protected Sessao buildSessaoAbertaEntity(Long pautaId, Long sessaoId) {
        return Sessao.builder()
                .id(sessaoId)
                .dataFinal(now().plusSeconds(60))
                .pauta(buildPautaEntity(pautaId))
                .build();
    }

    protected Sessao buildSessaoEncerradaEntity(Long pautaId, Long sessaoId) {
        return Sessao.builder()
                .id(sessaoId)
                .dataFinal(now().minusSeconds(1))
                .pauta(buildPautaEntity(pautaId))
                .build();
    }

    protected Associado buildAssociadoEntity(Long associadoId) {
        return Associado.builder()
                .id(associadoId)
                .build();
    }

    protected VotoRequest buildVotoRequest(Long associadoId, VotoComputado votoComputado) {
        var request = new VotoRequest();
        request.setIdAssociado(associadoId);
        request.setVoto(votoComputado);
        return request;
    }

    protected VotoResultado buildVotoResultado(String computado, int total) {
        return new VotoResultado() {
            @Override
            public String getComputado() {
                return computado;
            }

            @Override
            public int getTotal() {
                return total;
            }
        };
    }
}

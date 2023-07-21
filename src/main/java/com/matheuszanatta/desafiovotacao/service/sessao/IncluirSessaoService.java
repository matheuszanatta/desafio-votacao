package com.matheuszanatta.desafiovotacao.service.sessao;

import com.matheuszanatta.desafiovotacao.controller.dto.request.SessaoRequest;
import com.matheuszanatta.desafiovotacao.controller.dto.response.SessaoResponse;
import com.matheuszanatta.desafiovotacao.domain.Pauta;
import com.matheuszanatta.desafiovotacao.domain.Sessao;
import com.matheuszanatta.desafiovotacao.exception.RegraNegocioException;
import com.matheuszanatta.desafiovotacao.repository.SessaoRepository;
import com.matheuszanatta.desafiovotacao.service.pauta.BuscarPautaService;
import com.matheuszanatta.desafiovotacao.util.BuscarMensagemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.matheuszanatta.desafiovotacao.mapper.SessaoMapper.toResponse;
import static java.util.Objects.nonNull;

@RequiredArgsConstructor
@Service
@Slf4j
public class IncluirSessaoService {

    private final SessaoRepository sessaoRepository;
    private final BuscarPautaService buscarPautaService;
    private final BuscarMensagemService buscarMensagemService;

    @Value("${app.sessao.duracao}")
    private Integer duracaoPadrao;

    @Transactional
    public SessaoResponse incluir(Long idPauta, SessaoRequest request) {

        var pauta = buscarPautaService.porId(idPauta);

        validarSessaoExistente(idPauta);

        var duracao = definirDuracao(request);

        var sessao = buildEntity(pauta, duracao);

        sessaoRepository.save(sessao);

        log.info("Sessão {} incluída com sucesso", sessao.getId());

        return toResponse(sessao);
    }

    private void validarSessaoExistente(Long idPauta) {
        var existeSessao = sessaoRepository.existsByPautaId(idPauta);
        if (existeSessao) {
            throw new RegraNegocioException(buscarMensagemService.porChave("erro.sessao.duplicada"));
        }
    }

    private Integer definirDuracao(SessaoRequest request) {
        return nonNull(request.getDuracao()) ? request.getDuracao() : duracaoPadrao;
    }

    private Sessao buildEntity(Pauta pauta, Integer duracao) {
        var now = LocalDateTime.now();
        return Sessao.builder()
                .pauta(pauta)
                .dataInicial(now)
                .dataFinal(now.plusSeconds(duracao))
                .build();
    }
}

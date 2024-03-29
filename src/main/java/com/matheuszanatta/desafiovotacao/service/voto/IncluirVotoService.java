package com.matheuszanatta.desafiovotacao.service.voto;

import com.matheuszanatta.desafiovotacao.controller.dto.request.VotoRequest;
import com.matheuszanatta.desafiovotacao.controller.dto.response.VotoResponse;
import com.matheuszanatta.desafiovotacao.domain.Associado;
import com.matheuszanatta.desafiovotacao.domain.Pauta;
import com.matheuszanatta.desafiovotacao.domain.Voto;
import com.matheuszanatta.desafiovotacao.exception.RegraNegocioException;
import com.matheuszanatta.desafiovotacao.repository.VotoRepository;
import com.matheuszanatta.desafiovotacao.service.associado.BuscarAssociadoService;
import com.matheuszanatta.desafiovotacao.service.pauta.BuscarPautaService;
import com.matheuszanatta.desafiovotacao.service.sessao.BuscarSessaoService;
import com.matheuszanatta.desafiovotacao.util.BuscarMensagemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Slf4j
public class IncluirVotoService {

    private final BuscarPautaService buscarPautaService;
    private final BuscarSessaoService buscarSessaoService;
    private final BuscarAssociadoService buscarAssociadoService;
    private final VotoRepository votoRepository;
    private final BuscarMensagemService buscarMensagemService;

    @Transactional
    public VotoResponse incluir(Long idPauta, VotoRequest request) {

        var pauta = buscarPautaService.porId(idPauta);

        validarSessaoAberta(idPauta);

        var associado = buscarAssociadoService.porId(request.getIdAssociado());

        validarVotoUnico(pauta, associado);

        var voto = buildEntity(request, pauta, associado);

        votoRepository.save(voto);

        log.info("Voto {} incluído com sucesso", voto.getId());

        return buildResponse(voto);
    }

    private void validarSessaoAberta(Long idPauta) {
        var sessao = buscarSessaoService.porIdPauta(idPauta);
        if (sessao.isEncerrada()) {
            throw new RegraNegocioException(buscarMensagemService.porChave("erro.sessao.encerrada"));
        }
    }

    private void validarVotoUnico(Pauta pauta, Associado associado) {
        var jaVotou = votoRepository.existsByAssociadoIdAndPautaId(associado.getId(), pauta.getId());
        if (jaVotou) {
            throw new RegraNegocioException(buscarMensagemService.porChave("erro.voto.duplicado"));
        }
    }

    private Voto buildEntity(VotoRequest request, Pauta pauta, Associado associado) {
        return Voto.builder()
                .pauta(pauta)
                .associado(associado)
                .votoComputado(request.getVoto())
                .build();
    }

    private VotoResponse buildResponse(Voto voto) {
        return VotoResponse.builder()
                .id(voto.getId())
                .build();
    }
}

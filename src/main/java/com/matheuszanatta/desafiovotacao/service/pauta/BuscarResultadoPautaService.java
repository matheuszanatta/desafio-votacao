package com.matheuszanatta.desafiovotacao.service.pauta;

import com.matheuszanatta.desafiovotacao.controller.dto.response.PautaResultadoResponse;
import com.matheuszanatta.desafiovotacao.domain.Sessao;
import com.matheuszanatta.desafiovotacao.domain.enums.ResultadoVoto;
import com.matheuszanatta.desafiovotacao.domain.enums.VotoComputado;
import com.matheuszanatta.desafiovotacao.repository.VotoRepository;
import com.matheuszanatta.desafiovotacao.repository.projection.VotoResultado;
import com.matheuszanatta.desafiovotacao.service.sessao.BuscarSessaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.matheuszanatta.desafiovotacao.domain.enums.ResultadoVoto.*;
import static com.matheuszanatta.desafiovotacao.domain.enums.VotoComputado.NAO;
import static com.matheuszanatta.desafiovotacao.domain.enums.VotoComputado.SIM;
import static java.lang.Integer.compare;

@RequiredArgsConstructor
@Service
@Slf4j
public class BuscarResultadoPautaService {

    private final BuscarPautaService buscarPautaService;
    private final BuscarSessaoService buscarSessaoService;
    private final VotoRepository votoRepository;

    public PautaResultadoResponse buscar(Long id) {

        buscarPautaService.validarPorId(id);

        var sessao = buscarSessaoService.porIdPauta(id);
        var votosComputados = votoRepository.buscarVotosComputadosPorPauta(id);

        var votoSim = getTotal(votosComputados, SIM);
        var votoNao = getTotal(votosComputados, NAO);
        var total = votoSim + votoNao;

        var resultado = definirResultado(sessao, total, votoSim, votoNao);

        log.info("Resultado da pauta {}: {}", id, resultado);

        return buildResponse(id, votoSim, votoNao, total, resultado);
    }

    private int getTotal(List<VotoResultado> votos, VotoComputado votoComputado) {
        return votos.stream()
                .filter(voto -> Objects.equals(voto.getComputado(), Objects.toString(votoComputado)))
                .findFirst()
                .map(VotoResultado::getTotal)
                .orElse(0);
    }

    private ResultadoVoto definirResultado(Sessao sessao, int total, int votoSim, int votoNao) {

        if (sessao.isAberta()) {
            return EM_ANDAMENTO;
        }

        if (total == 0) {
            return SEM_VOTOS;
        }

        return switch (compare(votoSim, votoNao)) {
            case 1 -> APROVADA;
            case -1 -> REPROVADA;
            default -> EMPATE;
        };
    }

    private PautaResultadoResponse buildResponse(Long id, int votoSim, int votoNao, int total, ResultadoVoto resultado) {
        return PautaResultadoResponse.builder()
                .id(id)
                .votos(total)
                .votosSim(votoSim)
                .votosNao(votoNao)
                .resultado(resultado)
                .build();
    }
}

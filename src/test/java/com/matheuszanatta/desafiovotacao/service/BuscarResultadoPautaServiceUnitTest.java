package com.matheuszanatta.desafiovotacao.service;

import com.matheuszanatta.desafiovotacao.BaseUnitTest;
import com.matheuszanatta.desafiovotacao.exception.RecursoNaoEncontradoException;
import com.matheuszanatta.desafiovotacao.repository.VotoRepository;
import com.matheuszanatta.desafiovotacao.service.pauta.BuscarPautaService;
import com.matheuszanatta.desafiovotacao.service.pauta.BuscarResultadoPautaService;
import com.matheuszanatta.desafiovotacao.service.sessao.BuscarSessaoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.matheuszanatta.desafiovotacao.domain.enums.ResultadoVoto.*;
import static com.matheuszanatta.desafiovotacao.domain.enums.VotoComputado.NAO;
import static com.matheuszanatta.desafiovotacao.domain.enums.VotoComputado.SIM;
import static java.util.Arrays.asList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BuscarResultadoPautaServiceUnitTest extends BaseUnitTest {

    @InjectMocks
    private BuscarResultadoPautaService tested;

    @Mock
    private BuscarPautaService buscarPautaService;

    @Mock
    private BuscarSessaoService buscarSessaoService;

    @Mock
    private VotoRepository votoRepository;

    @Test
    @DisplayName("Pauta com resultado aprovado")
    void pautaAprovada() {

        var pautaId = 1L;
        var sessaoId = 1L;
        var votosResultadoSim = buildVotoResultado(SIM.name(), 2);
        var votosResultadoNao = buildVotoResultado(NAO.name(), 1);
        var totalVotos = votosResultadoSim.getTotal() + votosResultadoNao.getTotal();

        when(buscarSessaoService.porIdPauta(pautaId)).thenReturn(buildSessaoEncerradaEntity(pautaId, sessaoId));
        when(votoRepository.buscarVotosComputadosPorPauta(pautaId)).thenReturn(asList(votosResultadoSim, votosResultadoNao));

        var actual = tested.buscar(pautaId);

        verify(buscarPautaService).validarPorId(pautaId);
        verify(buscarSessaoService).porIdPauta(pautaId);
        verify(votoRepository).buscarVotosComputadosPorPauta(pautaId);

        assertNotNull(actual);
        assertEquals(pautaId, actual.getId());
        assertEquals(totalVotos, actual.getVotos());
        assertEquals(votosResultadoSim.getTotal(), actual.getVotosSim());
        assertEquals(votosResultadoNao.getTotal(), actual.getVotosNao());
        assertEquals(APROVADA, actual.getResultado());
    }

    @Test
    @DisplayName("Pauta com resultado reprovado")
    void pautaReprovada() {

        var pautaId = 1L;
        var sessaoId = 1L;
        var votosResultadoSim = buildVotoResultado(SIM.name(), 1);
        var votosResultadoNao = buildVotoResultado(NAO.name(), 2);

        when(buscarSessaoService.porIdPauta(pautaId)).thenReturn(buildSessaoEncerradaEntity(pautaId, sessaoId));
        when(votoRepository.buscarVotosComputadosPorPauta(pautaId)).thenReturn(asList(votosResultadoSim, votosResultadoNao));

        var actual = tested.buscar(pautaId);

        assertNotNull(actual);
        assertEquals(REPROVADA, actual.getResultado());
    }

    @Test
    @DisplayName("Pauta com resultado empatado")
    void pautaEmpatada() {

        var pautaId = 1L;
        var sessaoId = 1L;
        var votosResultadoSim = buildVotoResultado(SIM.name(), 2);
        var votosResultadoNao = buildVotoResultado(NAO.name(), 2);

        when(buscarSessaoService.porIdPauta(pautaId)).thenReturn(buildSessaoEncerradaEntity(pautaId, sessaoId));
        when(votoRepository.buscarVotosComputadosPorPauta(pautaId)).thenReturn(asList(votosResultadoSim, votosResultadoNao));

        var actual = tested.buscar(pautaId);

        assertNotNull(actual);
        assertEquals(EMPATE, actual.getResultado());
    }

    @Test
    @DisplayName("Pauta com resultado em andamento")
    void pautaEmAndamento() {

        var pautaId = 1L;
        var sessaoId = 1L;
        var votosResultadoSim = buildVotoResultado(SIM.name(), 2);
        var votosResultadoNao = buildVotoResultado(NAO.name(), 1);

        when(buscarSessaoService.porIdPauta(pautaId)).thenReturn(buildSessaoAbertaEntity(pautaId, sessaoId));
        when(votoRepository.buscarVotosComputadosPorPauta(pautaId)).thenReturn(asList(votosResultadoSim, votosResultadoNao));

        var actual = tested.buscar(pautaId);

        assertNotNull(actual);
        assertEquals(EM_ANDAMENTO, actual.getResultado());
    }

    @Test
    @DisplayName("Pauta com resultado sem votos")
    void pautaSemVotos() {

        var pautaId = 1L;
        var sessaoId = 1L;
        var votosResultadoSim = buildVotoResultado(SIM.name(), 0);
        var votosResultadoNao = buildVotoResultado(NAO.name(), 0);
        var totalVotos = votosResultadoSim.getTotal() + votosResultadoNao.getTotal();

        when(buscarSessaoService.porIdPauta(pautaId)).thenReturn(buildSessaoEncerradaEntity(pautaId, sessaoId));
        when(votoRepository.buscarVotosComputadosPorPauta(pautaId)).thenReturn(asList(votosResultadoSim, votosResultadoNao));

        var actual = tested.buscar(pautaId);

        assertNotNull(actual);
        assertEquals(pautaId, actual.getId());
        assertEquals(totalVotos, actual.getVotos());
        assertEquals(votosResultadoSim.getTotal(), actual.getVotosSim());
        assertEquals(votosResultadoNao.getTotal(), actual.getVotosNao());
        assertEquals(SEM_VOTOS, actual.getResultado());
    }

    @Test
    @DisplayName("Deve lançar erro quando não encontra pauta")
    void deveLancarErroQuandoNaoEncontraPauta() {

        var pautaId = 1L;

        doThrow(RecursoNaoEncontradoException.class).when(buscarPautaService).validarPorId(pautaId);

        assertThrows(RecursoNaoEncontradoException.class, () -> tested.buscar(pautaId));
    }

    @Test
    @DisplayName("Deve lançar erro quando não encontra sessão")
    void deveLancarErroQuandoNaoEncontraSessao() {

        var pautaId = 1L;

        when(buscarSessaoService.porIdPauta(pautaId)).thenThrow(RecursoNaoEncontradoException.class);

        assertThrows(RecursoNaoEncontradoException.class, () -> tested.buscar(pautaId));
    }

}

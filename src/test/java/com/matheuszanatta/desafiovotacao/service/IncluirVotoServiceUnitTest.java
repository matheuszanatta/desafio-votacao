package com.matheuszanatta.desafiovotacao.service;

import com.matheuszanatta.desafiovotacao.BaseUnitTest;
import com.matheuszanatta.desafiovotacao.controller.dto.request.VotoRequest;
import com.matheuszanatta.desafiovotacao.domain.Voto;
import com.matheuszanatta.desafiovotacao.exception.RecursoNaoEncontradoException;
import com.matheuszanatta.desafiovotacao.exception.RegraNegocioException;
import com.matheuszanatta.desafiovotacao.repository.VotoRepository;
import com.matheuszanatta.desafiovotacao.service.associado.BuscarAssociadoService;
import com.matheuszanatta.desafiovotacao.service.pauta.BuscarPautaService;
import com.matheuszanatta.desafiovotacao.service.sessao.BuscarSessaoService;
import com.matheuszanatta.desafiovotacao.service.voto.IncluirVotoService;
import com.matheuszanatta.desafiovotacao.util.BuscarMensagemService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.matheuszanatta.desafiovotacao.domain.enums.VotoComputado.SIM;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IncluirVotoServiceUnitTest extends BaseUnitTest {

    @InjectMocks
    private IncluirVotoService tested;

    @Mock
    private BuscarPautaService buscarPautaService;

    @Mock
    private BuscarSessaoService buscarSessaoService;

    @Mock
    private BuscarAssociadoService buscarAssociadoService;

    @Mock
    private VotoRepository votoRepository;

    @Mock
    private BuscarMensagemService buscarMensagemService;

    @Captor
    private ArgumentCaptor<Voto> votoCaptor;

    @Test
    @DisplayName("Deve votar com sucesso")
    void deveVotarComSucesso() {

        var pautaId = 1L;
        var sessaoId = 1L;
        var associadoId = 1L;
        var votoComputado = SIM;
        var request = buildVotoRequest(associadoId, votoComputado);

        when(buscarPautaService.porId(pautaId)).thenReturn(buildPautaEntity(pautaId));
        when(buscarSessaoService.porIdPauta(pautaId)).thenReturn(buildSessaoAbertaEntity(pautaId, sessaoId));
        when(buscarAssociadoService.porId(associadoId)).thenReturn(buildAssociadoEntity(associadoId));
        when(votoRepository.existsByAssociadoIdAndPautaId(associadoId, pautaId)).thenReturn(false);

        var actual = tested.incluir(pautaId, request);

        verify(buscarPautaService).porId(pautaId);
        verify(buscarSessaoService).porIdPauta(pautaId);
        verify(buscarAssociadoService).porId(associadoId);
        verify(votoRepository).existsByAssociadoIdAndPautaId(associadoId, pautaId);
        verify(votoRepository).save(votoCaptor.capture());

        var voto = votoCaptor.getValue();

        assertNotNull(actual);
        assertEquals(pautaId, voto.getPauta().getId());
        assertEquals(associadoId, voto.getAssociado().getId());
        assertEquals(votoComputado, voto.getVotoComputado());
    }

    @Test
    @DisplayName("Deve lançar erro quando não encontra pauta")
    void deveLancarErroQuandoNaoEncontraPauta() {

        var pautaId = 1L;
        var request = new VotoRequest();

        when(buscarPautaService.porId(pautaId)).thenThrow(RecursoNaoEncontradoException.class);

        assertThrows(RecursoNaoEncontradoException.class, () -> tested.incluir(pautaId, request));
    }

    @Test
    @DisplayName("Deve lançar erro quando não encontra sessão")
    void deveLancarErroQuandoNaoEncontraSessao() {

        var pautaId = 1L;
        var request = new VotoRequest();

        when(buscarSessaoService.porIdPauta(pautaId)).thenThrow(RecursoNaoEncontradoException.class);

        assertThrows(RecursoNaoEncontradoException.class, () -> tested.incluir(pautaId, request));
    }

    @Test
    @DisplayName("Deve lançar erro ao votar em sessão encerrada")
    void deveLancarErroAoVotarEmSessaoEncerrada() {

        var pautaId = 1L;
        var sessaoId = 1L;
        var request = new VotoRequest();

        when(buscarSessaoService.porIdPauta(pautaId)).thenReturn(buildSessaoEncerradaEntity(pautaId, sessaoId));

        assertThrows(RegraNegocioException.class, () -> tested.incluir(pautaId, request));
    }

    @Test
    @DisplayName("Deve lançar erro quando não encontra associado")
    void deveLancarErroQuandoNaoEncontraAssociado() {

        var pautaId = 1L;
        var sessaoId = 1L;
        var request = buildVotoRequest(1L, SIM);

        when(buscarSessaoService.porIdPauta(pautaId)).thenReturn(buildSessaoAbertaEntity(pautaId, sessaoId));
        when(buscarAssociadoService.porId(request.getIdAssociado())).thenThrow(RecursoNaoEncontradoException.class);

        assertThrows(RecursoNaoEncontradoException.class, () -> tested.incluir(pautaId, request));
    }

    @Test
    @DisplayName("Deve lançar erro ao votar duas vezes")
    void deveLancarErroAoVotarDuasVezes() {

        var pautaId = 1L;
        var sessaoId = 1L;
        var associadoId = 1L;
        var request = buildVotoRequest(associadoId, SIM);

        when(buscarPautaService.porId(pautaId)).thenReturn(buildPautaEntity(pautaId));
        when(buscarSessaoService.porIdPauta(pautaId)).thenReturn(buildSessaoAbertaEntity(pautaId, sessaoId));
        when(buscarAssociadoService.porId(associadoId)).thenReturn(buildAssociadoEntity(associadoId));
        when(votoRepository.existsByAssociadoIdAndPautaId(associadoId, pautaId)).thenReturn(true);

        assertThrows(RegraNegocioException.class, () -> tested.incluir(pautaId, request));
    }
}

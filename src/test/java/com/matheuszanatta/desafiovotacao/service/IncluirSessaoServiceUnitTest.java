package com.matheuszanatta.desafiovotacao.service;

import com.matheuszanatta.desafiovotacao.BaseUnitTest;
import com.matheuszanatta.desafiovotacao.controller.dto.request.SessaoRequest;
import com.matheuszanatta.desafiovotacao.domain.Sessao;
import com.matheuszanatta.desafiovotacao.exception.RecursoNaoEncontradoException;
import com.matheuszanatta.desafiovotacao.repository.SessaoRepository;
import com.matheuszanatta.desafiovotacao.service.pauta.BuscarPautaService;
import com.matheuszanatta.desafiovotacao.service.sessao.IncluirSessaoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.util.ReflectionTestUtils.setField;

@ExtendWith(MockitoExtension.class)
class IncluirSessaoServiceUnitTest extends BaseUnitTest {

    @InjectMocks
    private IncluirSessaoService tested;
    @Mock
    private SessaoRepository sessaoRepository;
    @Mock
    private BuscarPautaService buscarPautaService;
    @Captor
    private ArgumentCaptor<Sessao> sessaoCaptor;

    @Test
    @DisplayName("Deve incluir com sucesso")
    void deveIncluirComSucesso() {

        var pautaId = 1L;
        var request = new SessaoRequest();
        var duracaoPadrao = 3;

        when(buscarPautaService.porId(pautaId)).thenReturn(buildPautaEntity(pautaId));
        when(sessaoRepository.existsByPautaId(pautaId)).thenReturn(false);
        setField(tested, "duracaoPadrao", duracaoPadrao);

        var actual = tested.incluir(pautaId, request);

        verify(buscarPautaService).porId(pautaId);
        verify(sessaoRepository).existsByPautaId(pautaId);
        verify(sessaoRepository).save(sessaoCaptor.capture());

        var sessao = sessaoCaptor.getValue();

        assertNotNull(actual);
        assertEquals(pautaId, sessao.getPauta().getId());
    }

    @Test
    @DisplayName("Deve lançar erro quando não encontra pauta")
    void deveLancarErroQuandoNaoEncontraPauta() {

        var pautaId = 1L;
        var request = new SessaoRequest();

        when(buscarPautaService.porId(pautaId)).thenThrow(RecursoNaoEncontradoException.class);

        assertThrows(RecursoNaoEncontradoException.class, () -> tested.incluir(pautaId, request));
    }

    @Test
    @DisplayName("Deve lançar erro quando já existe sessão")
    void deveLancarErroQuandoJaExisteSessao() {

        var pautaId = 1L;
        var request = new SessaoRequest();

        when(sessaoRepository.existsByPautaId(pautaId)).thenThrow(RecursoNaoEncontradoException.class);

        assertThrows(RecursoNaoEncontradoException.class, () -> tested.incluir(pautaId, request));
    }
}

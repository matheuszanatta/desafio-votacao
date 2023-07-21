package com.matheuszanatta.desafiovotacao.service.sessao;

import com.matheuszanatta.desafiovotacao.domain.Sessao;
import com.matheuszanatta.desafiovotacao.exception.RecursoNaoEncontradoException;
import com.matheuszanatta.desafiovotacao.repository.SessaoRepository;
import com.matheuszanatta.desafiovotacao.util.BuscarMensagemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BuscarSessaoService {

    private final SessaoRepository repository;
    private final BuscarMensagemService buscarMensagemService;

    public Sessao porIdPauta(Long idPauta) {
        return repository.findByPautaIdFetch(idPauta)
                .orElseThrow(() -> new RecursoNaoEncontradoException(buscarMensagemService.porChave("erro.sessao.nao.encontrada")));
    }
}

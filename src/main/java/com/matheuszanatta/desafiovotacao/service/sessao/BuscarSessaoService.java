package com.matheuszanatta.desafiovotacao.service.sessao;

import com.matheuszanatta.desafiovotacao.domain.Sessao;
import com.matheuszanatta.desafiovotacao.exception.RecursoNaoEncontradoException;
import com.matheuszanatta.desafiovotacao.repository.SessaoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BuscarSessaoService {

    private final SessaoRepository repository;

    public Sessao porIdPauta(Long idPauta) {
        return repository.findByPautaIdFetch(idPauta)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Sessão não encontrada"));
    }
}

package com.matheuszanatta.desafiovotacao.service.pauta;

import com.matheuszanatta.desafiovotacao.domain.Pauta;
import com.matheuszanatta.desafiovotacao.exception.RecursoNaoEncontradoException;
import com.matheuszanatta.desafiovotacao.repository.PautaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BuscarPautaService {

    private final PautaRepository repository;

    public Pauta porId(Long id) {
        return repository.findById(id)
                .orElseThrow(this::buildErroNaoEncontrado);
    }

    public void validarPorId(Long id) {
        var existe = repository.existsById(id);
        if (!existe) {
            throw buildErroNaoEncontrado();
        }
    }

    private RecursoNaoEncontradoException buildErroNaoEncontrado() {
        return new RecursoNaoEncontradoException("Pauta não encontrada");
    }
}

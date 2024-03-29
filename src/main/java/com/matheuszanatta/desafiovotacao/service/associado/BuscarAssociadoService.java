package com.matheuszanatta.desafiovotacao.service.associado;

import com.matheuszanatta.desafiovotacao.domain.Associado;
import com.matheuszanatta.desafiovotacao.exception.RecursoNaoEncontradoException;
import com.matheuszanatta.desafiovotacao.repository.AssociadoRepository;
import com.matheuszanatta.desafiovotacao.util.BuscarMensagemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BuscarAssociadoService {

    private final AssociadoRepository repository;
    private final BuscarMensagemService buscarMensagemService;

    public Associado porId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException(buscarMensagemService.porChave("erro.associado.nao.encontrado")));
    }
}

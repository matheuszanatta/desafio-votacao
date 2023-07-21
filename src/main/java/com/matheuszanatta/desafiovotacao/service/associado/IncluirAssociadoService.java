package com.matheuszanatta.desafiovotacao.service.associado;

import com.matheuszanatta.desafiovotacao.controller.dto.request.AssociadoRequest;
import com.matheuszanatta.desafiovotacao.controller.dto.response.AssociadoResponse;
import com.matheuszanatta.desafiovotacao.domain.Associado;
import com.matheuszanatta.desafiovotacao.exception.RegraNegocioException;
import com.matheuszanatta.desafiovotacao.repository.AssociadoRepository;
import com.matheuszanatta.desafiovotacao.util.BuscarMensagemService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.matheuszanatta.desafiovotacao.mapper.AssociadoMapper.toEntity;
import static com.matheuszanatta.desafiovotacao.mapper.AssociadoMapper.toResponse;

@RequiredArgsConstructor
@Service
@Slf4j
public class IncluirAssociadoService {

    private final AssociadoRepository repository;
    private final BuscarMensagemService buscarMensagemService;

    @Transactional
    public AssociadoResponse incluir(AssociadoRequest request) {

        var associado = toEntity(request);

        validarAssociadoExistente(associado);

        repository.save(associado);

        log.info("Associado {} incluído com sucesso", associado.getId());

        return toResponse(associado);
    }

    private void validarAssociadoExistente(Associado associado) {
        var existeAssociado = repository.existsByCpf(associado.getCpf());
        if (existeAssociado) {
            throw new RegraNegocioException(buscarMensagemService.porChave("erro.associado.cpf.duplicado"));
        }
    }
}

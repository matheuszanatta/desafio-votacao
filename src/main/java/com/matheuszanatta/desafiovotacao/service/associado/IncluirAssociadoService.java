package com.matheuszanatta.desafiovotacao.service.associado;

import com.matheuszanatta.desafiovotacao.controller.dto.request.AssociadoRequest;
import com.matheuszanatta.desafiovotacao.controller.dto.response.AssociadoResponse;
import com.matheuszanatta.desafiovotacao.domain.Associado;
import com.matheuszanatta.desafiovotacao.exception.RegraNegocioException;
import com.matheuszanatta.desafiovotacao.repository.AssociadoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.matheuszanatta.desafiovotacao.mapper.AssociadoMapper.toEntity;
import static com.matheuszanatta.desafiovotacao.mapper.AssociadoMapper.toResponse;

@RequiredArgsConstructor
@Service
public class IncluirAssociadoService {

    private final AssociadoRepository repository;

    @Transactional
    public AssociadoResponse incluir(AssociadoRequest request) {

        var associado = toEntity(request);

        validarAssociadoExistente(associado);

        repository.save(associado);

        return toResponse(associado);
    }

    private void validarAssociadoExistente(Associado associado) {
        var existeAssociado = repository.existsByCpf(associado.getCpf());
        if (existeAssociado) {
            throw new RegraNegocioException("Já existe um associado com este CPF");
        }
    }
}

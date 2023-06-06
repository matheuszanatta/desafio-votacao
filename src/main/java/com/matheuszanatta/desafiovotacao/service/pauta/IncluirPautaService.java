package com.matheuszanatta.desafiovotacao.service.pauta;

import com.matheuszanatta.desafiovotacao.controller.dto.request.PautaRequest;
import com.matheuszanatta.desafiovotacao.controller.dto.response.PautaResponse;
import com.matheuszanatta.desafiovotacao.repository.PautaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.matheuszanatta.desafiovotacao.mapper.PautaMapper.toEntity;
import static com.matheuszanatta.desafiovotacao.mapper.PautaMapper.toResponse;

@RequiredArgsConstructor
@Service
public class IncluirPautaService {

    private final PautaRepository repository;

    @Transactional
    public PautaResponse incluir(PautaRequest request) {
        var pauta = repository.save(toEntity(request));
        return toResponse(pauta);
    }
}

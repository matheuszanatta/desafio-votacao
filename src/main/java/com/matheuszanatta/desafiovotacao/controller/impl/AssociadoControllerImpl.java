package com.matheuszanatta.desafiovotacao.controller.impl;

import com.matheuszanatta.desafiovotacao.controller.AssociadoController;
import com.matheuszanatta.desafiovotacao.controller.dto.request.AssociadoRequest;
import com.matheuszanatta.desafiovotacao.controller.dto.response.AssociadoResponse;
import com.matheuszanatta.desafiovotacao.service.associado.IncluirAssociadoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/associados")
public class AssociadoControllerImpl implements AssociadoController {

    private final IncluirAssociadoService incluirAssociadoService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public AssociadoResponse incluirAssociado(@Valid @RequestBody AssociadoRequest request) {
        return incluirAssociadoService.incluir(request);
    }
}

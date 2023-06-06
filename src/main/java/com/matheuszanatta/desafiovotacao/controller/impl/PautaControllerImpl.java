package com.matheuszanatta.desafiovotacao.controller.impl;

import com.matheuszanatta.desafiovotacao.controller.PautaController;
import com.matheuszanatta.desafiovotacao.controller.dto.request.PautaRequest;
import com.matheuszanatta.desafiovotacao.controller.dto.request.SessaoRequest;
import com.matheuszanatta.desafiovotacao.controller.dto.request.VotoRequest;
import com.matheuszanatta.desafiovotacao.controller.dto.response.PautaResponse;
import com.matheuszanatta.desafiovotacao.controller.dto.response.PautaResultadoResponse;
import com.matheuszanatta.desafiovotacao.controller.dto.response.SessaoResponse;
import com.matheuszanatta.desafiovotacao.controller.dto.response.VotoResponse;
import com.matheuszanatta.desafiovotacao.service.pauta.BuscarResultadoPautaService;
import com.matheuszanatta.desafiovotacao.service.pauta.IncluirPautaService;
import com.matheuszanatta.desafiovotacao.service.sessao.IncluirSessaoService;
import com.matheuszanatta.desafiovotacao.service.voto.IncluirVotoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/pautas")
public class PautaControllerImpl implements PautaController {

    private final IncluirPautaService incluirPautaService;
    private final IncluirSessaoService incluirSessaoService;
    private final IncluirVotoService incluirVotoService;
    private final BuscarResultadoPautaService buscarResultadoPautaService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public PautaResponse incluirPauta(@Valid @RequestBody PautaRequest request) {
        return incluirPautaService.incluir(request);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/{idPauta}/sessao")
    public SessaoResponse incluirSessao(@PathVariable Long idPauta,
                                        @RequestBody(required = false) SessaoRequest request) {
        return incluirSessaoService.incluir(idPauta, request);
    }

    @PostMapping("/{idPauta}/voto")
    public VotoResponse votarPauta(@PathVariable Long idPauta,
                                   @Valid @RequestBody VotoRequest request) {
        return incluirVotoService.incluir(idPauta, request);
    }

    @GetMapping("/{id}/resultado")
    public PautaResultadoResponse buscarResultadoPauta(@PathVariable Long id) {
        return buscarResultadoPautaService.buscar(id);
    }
}

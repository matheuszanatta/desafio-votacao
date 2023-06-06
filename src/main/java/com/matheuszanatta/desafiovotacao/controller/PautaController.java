package com.matheuszanatta.desafiovotacao.controller;

import com.matheuszanatta.desafiovotacao.controller.dto.request.PautaRequest;
import com.matheuszanatta.desafiovotacao.controller.dto.request.SessaoRequest;
import com.matheuszanatta.desafiovotacao.controller.dto.request.VotoRequest;
import com.matheuszanatta.desafiovotacao.controller.dto.response.PautaResponse;
import com.matheuszanatta.desafiovotacao.controller.dto.response.PautaResultadoResponse;
import com.matheuszanatta.desafiovotacao.controller.dto.response.SessaoResponse;
import com.matheuszanatta.desafiovotacao.controller.dto.response.VotoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Pauta", description = "Endpoints para gerenciamento de pautas")
public interface PautaController {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pauta criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "500", description = "Falha interna do servidor"),
    })
    @Operation(summary = "Incluir nova pauta")
    PautaResponse incluirPauta(PautaRequest request);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pauta criada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado"),
            @ApiResponse(responseCode = "422", description = "Já existe uma sessão para esta pauta"),
            @ApiResponse(responseCode = "500", description = "Falha interna do servidor"),
    })
    @Operation(summary = "Incluir nova sessão")
    SessaoResponse incluirSessao(Long idPauta,
                                 SessaoRequest request);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Voto computado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado"),
            @ApiResponse(responseCode = "422", description = "Sessão de votação encerrada | Associado já votou nesta pauta"),
            @ApiResponse(responseCode = "500", description = "Falha interna do servidor"),
    })
    @Operation(summary = "Votar na pauta")
    VotoResponse votarPauta(Long idPauta,
                            VotoRequest request);

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resultado da pauta retornado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado"),
            @ApiResponse(responseCode = "422", description = "Pauta ainda não foi votada"),
            @ApiResponse(responseCode = "500", description = "Falha interna do servidor"),
    })
    @Operation(summary = "Buscar o resultado da pauta")
    PautaResultadoResponse buscarResultadoPauta(Long id);
}

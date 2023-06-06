package com.matheuszanatta.desafiovotacao.controller;

import com.matheuszanatta.desafiovotacao.controller.dto.request.AssociadoRequest;
import com.matheuszanatta.desafiovotacao.controller.dto.response.AssociadoResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Associado", description = "Endpoints para gerenciamento de associados")
public interface AssociadoController {

    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Associado criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida"),
            @ApiResponse(responseCode = "422", description = "Já existe um associado com este CPF"),
            @ApiResponse(responseCode = "500", description = "Falha interna do servidor"),
    })
    @Operation(summary = "Incluir novo associado")
    AssociadoResponse incluirAssociado(AssociadoRequest request);
}

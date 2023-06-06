package com.matheuszanatta.desafiovotacao.controller.dto.request;

import com.matheuszanatta.desafiovotacao.domain.enums.VotoComputado;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class VotoRequest {

    @NotNull(message = "O campo idAssociado não pode ser nulo")
    private Long idAssociado;

    @NotNull(message = "O campo voto não pode ser nulo")
    private VotoComputado voto;
}

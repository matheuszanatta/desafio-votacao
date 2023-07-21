package com.matheuszanatta.desafiovotacao.util;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import static java.util.Locale.getDefault;

@Component
@RequiredArgsConstructor
public class BuscarMensagemService {

    private final MessageSource messageSource;

    public String porChave(String chave) {
        return messageSource.getMessage(chave, null, getDefault());
    }
}

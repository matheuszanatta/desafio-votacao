package com.matheuszanatta.desafiovotacao.service.pauta;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PublicarPautaEncerradaService {

    private final RabbitTemplate rabbitTemplate;

    public void publicarMensagem(String message) {
        rabbitTemplate.convertAndSend(message);
    }
}

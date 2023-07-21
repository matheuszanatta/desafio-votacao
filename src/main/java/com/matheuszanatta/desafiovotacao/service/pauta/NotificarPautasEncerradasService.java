package com.matheuszanatta.desafiovotacao.service.pauta;

import com.matheuszanatta.desafiovotacao.controller.dto.response.PautaResultadoResponse;
import com.matheuszanatta.desafiovotacao.repository.SessaoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.matheuszanatta.desafiovotacao.config.MessageConfig.PAUTA_ENCERRADA_RK;
import static com.matheuszanatta.desafiovotacao.config.MessageConfig.PAUTA_EXCHANGE;


@RequiredArgsConstructor
@Service
@Slf4j
public class NotificarPautasEncerradasService {

    private final BuscarResultadoPautaService buscarResultadoPautaService;
    private final SessaoRepository sessaoRepository;
    private final RabbitTemplate rabbitTemplate;

    @Transactional
    public void notificar() {

        var sessoes = sessaoRepository.buscarSessoesEncerradasNaoEnviadas();

        sessoes.forEach(sessao -> {
            var resultado = buscarResultadoPautaService.buscar(sessao.getPauta().getId());

            var message = buildMensagem(resultado);
            rabbitTemplate.convertAndSend(PAUTA_EXCHANGE, PAUTA_ENCERRADA_RK, message);

            sessao.setEnviada(true);
            sessaoRepository.save(sessao);

            log.info("Pauta {} notificada com sucesso", sessao.getPauta().getId());
        });
    }

    private MensagemPautaEncerrada buildMensagem(PautaResultadoResponse resultado) {
        return MensagemPautaEncerrada.builder()
                .id(resultado.getId())
                .resultado(resultado.getResultado())
                .build();
    }
}

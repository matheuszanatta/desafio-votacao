package com.matheuszanatta.desafiovotacao.job;

import com.matheuszanatta.desafiovotacao.service.pauta.NotificarPautasEncerradasService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RequiredArgsConstructor
@Slf4j
public class PautaJob {

    private final NotificarPautasEncerradasService notificarPautasEncerradasService;

    @Scheduled(cron = "${app.job.cron}")
    public void buscarEPublicarEncerradas() {
        log.info("Executando job para buscar e publicar sessões encerradas.");
        notificarPautasEncerradasService.notificar();
    }
}

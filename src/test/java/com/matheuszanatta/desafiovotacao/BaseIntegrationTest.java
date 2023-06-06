package com.matheuszanatta.desafiovotacao;

import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.test.web.servlet.MvcResult;

import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static java.util.concurrent.Executors.newSingleThreadScheduledExecutor;

public abstract class BaseIntegrationTest {

    public static String toJson(Object obj) {
        try {
            return JsonMapper.builder()
                    .addModule(new JavaTimeModule())
                    .build()
                    .writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T toObject(MvcResult result, Class<T> clazz) {
        try {
            return toObject(result.getResponse().getContentAsString(), clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static <T> T toObject(String json, Class<T> clazz) {
        try {
            return JsonMapper.builder()
                    .addModule(new JavaTimeModule())
                    .build()
                    .readValue(json, clazz);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void executaAposDuracao(Runnable tarefa, int duracao) {
        ScheduledExecutorService executorService = newSingleThreadScheduledExecutor();
        executorService.schedule(tarefa, duracao, TimeUnit.SECONDS);

        executorService.shutdown();
        try {
            executorService.awaitTermination(duracao, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}

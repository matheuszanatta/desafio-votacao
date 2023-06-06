package com.matheuszanatta.desafiovotacao.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MessageConfig {

    public static final String PAUTA_EXCHANGE = "pauta.exchange";
    public static final String PAUTA_ENCERRADA_QUEUE = "pauta.encerrada.queue";
    public static final String PAUTA_ENCERRADA_RK = "encerrada";


    @Bean
    public TopicExchange pautaExchange() {
        return new TopicExchange(PAUTA_EXCHANGE);
    }

    @Bean
    public Queue pautaEncerradaQueue() {
        return new Queue(PAUTA_ENCERRADA_QUEUE);
    }

    @Bean
    public Binding declareBindingGeneric() {
        return BindingBuilder.bind(pautaEncerradaQueue()).to(pautaExchange()).with(PAUTA_ENCERRADA_RK);
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
        return rabbitTemplate;
    }

    @Bean
    public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}

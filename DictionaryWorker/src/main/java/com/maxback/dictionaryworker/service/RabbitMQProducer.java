package com.maxback.dictionaryworker.service;

import com.maxback.common.model.DictionaryWordData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RabbitMQProducer {

    @Value("${rabbitmq.exchange.name}")
    private String exchange;

    @Value("${rabbitmq.routing.key}")
    private String routingKey;

    private final RabbitTemplate rabbitTemplate;

    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(DictionaryWordData dictionaryWordData){
        log.info(String.format("Message sent -> %s", dictionaryWordData));
        rabbitTemplate.convertAndSend(exchange, routingKey, dictionaryWordData);
    }
}
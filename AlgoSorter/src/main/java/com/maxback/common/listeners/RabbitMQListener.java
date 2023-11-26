package com.maxback.common.listeners;

import com.maxback.common.model.DictionaryWordData;
import com.maxback.common.service.ClassifyingService;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Slf4j
@Service
public class RabbitMQListener {

    private final ClassifyingService classifyingService;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper MAPPER = new ObjectMapper();

    public RabbitMQListener(ClassifyingService classifyingService, RabbitTemplate rabbitTemplate) {
        this.classifyingService = classifyingService;
        this.rabbitTemplate = rabbitTemplate;
    }

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void consume(String entry) throws IOException {
        log.debug(String.format("Got message -> \"%s\"", entry));
        DictionaryWordData dictionaryWordData = MAPPER.readValue(entry, DictionaryWordData.class);
        try {
            classifyingService.analyzeWord(dictionaryWordData);
        }catch (Exception e){
            this.rabbitTemplate.send("words_queue.dlq", new Message(entry.getBytes()));
        }
    }

    @Bean
    public Queue parkingLot() {
        return new Queue("words_queue.dlq");
    }

}
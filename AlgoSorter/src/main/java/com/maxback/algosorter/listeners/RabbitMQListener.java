package com.maxback.algosorter.listeners;

import com.maxback.algosorter.model.DictionaryEntry;
import com.maxback.algosorter.service.ClassifyingService;
import lombok.extern.slf4j.Slf4j;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import java.io.IOException;


@Slf4j
@Service
public class RabbitMQListener {

    private final ClassifyingService classifyingService;
    private final ObjectMapper MAPPER = new ObjectMapper();
    public RabbitMQListener(ClassifyingService classifyingService) {
        this.classifyingService = classifyingService;
    }

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void consume(String entry) throws IOException {
        log.debug(String.format("Got message -> \"%s\"", entry));
        DictionaryEntry dictionaryEntry = MAPPER.readValue(entry, DictionaryEntry.class);
        classifyingService.analyzeWord(dictionaryEntry);
    }
}
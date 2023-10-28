package com.maxback.dictionaryworker;

import com.maxback.common.model.DictionaryWordData;
import com.maxback.common.model.PokemonData;
import com.maxback.dictionaryworker.service.DictionaryService;
import com.maxback.dictionaryworker.service.RabbitMQProducer;
import com.maxback.dictionaryworker.service.WordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;


@Slf4j
@EnableAsync
@SpringBootApplication
public class DictionaryWorkerApplication {

    final RabbitMQProducer rabbitProducer;
    final DictionaryService dictionaryService;
    final WordService wordService;


    public DictionaryWorkerApplication(RabbitMQProducer producer, DictionaryService dictionaryService, WordService wordService) {
        this.rabbitProducer = producer;
        this.dictionaryService = dictionaryService;
        this.wordService = wordService;
    }

    public static void main(String[] args) {
        SpringApplication.run(DictionaryWorkerApplication.class, args);
    }


    @Async
    @Scheduled(fixedRateString = "${scheduled.fixed.rate}")
    public void run() {
            String word = wordService.fetchRandomWordFromApi();
            DictionaryWordData dictionaryWordData = dictionaryService.getDictionaryWordData(word);
            PokemonData pokemonData = dictionaryService.getPokemonData(word);
            log.info("Pokemon name: " + word + " from type: " + pokemonData.getTypes().get(0));

            if (dictionaryWordData != null) {
                rabbitProducer.sendMessage(dictionaryWordData);
            }
    }

}

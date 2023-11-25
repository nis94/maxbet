package com.maxback.common;

import com.maxback.common.model.WordPojo;
import com.maxback.common.repository.WordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.util.List;


@SpringBootApplication
@Slf4j
public class AlgoSorterApplication {
    private final WordRepository wordRepository;
    private final RestTemplate restTemplate;

    @Value("${dictionary.api.url}")
    private String dictApiUrl;

    public AlgoSorterApplication(WordRepository wordRepository, RestTemplate restTemplate) {
        this.wordRepository = wordRepository;
        this.restTemplate = restTemplate;
    }

    public static void main(String[] args) {
        SpringApplication.run(AlgoSorterApplication.class, args);
    }

    // second, minute, hour, day of month, month, day(s) of week
    @Scheduled(cron = "0 */5 * * * *")
    public void run() {
        log.info("listing all words: ");
        List<WordPojo> wordFromDb = this.wordRepository.findAll();
        wordFromDb.stream()
                .sorted((w1, w2) -> w2.getWord().compareTo(w1.getWord()))
                .forEach(w -> System.out.println(w.getWord() + " x " + w.getCounter()));

        log.info("initialize word length...");
        restTemplate.postForEntity(dictApiUrl + "/3", null, String.class);
    }

}

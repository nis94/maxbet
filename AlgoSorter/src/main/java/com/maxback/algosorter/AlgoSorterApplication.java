package com.maxback.algosorter;

import com.maxback.algosorter.model.WordPojo;
import com.maxback.algosorter.repository.WordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;


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
    @Scheduled(cron = "0 0 * * * *")
    public void run() {
        log.info("run function started...");
        int length = 1;
        int maxLength = 1;
        ResponseEntity<String> response =
                restTemplate.getForEntity(dictApiUrl, String.class);

        if(response.getBody() != null) {
            maxLength = Integer.parseInt(response.getBody());
        }

        while (length < maxLength) {
            WordPojo wordFromDb = this.wordRepository.findByLength(length);
            if (wordFromDb != null) {
                log.info("Word in Length " + length + " = \"" + wordFromDb + "\"");
            }
            length++;
        }
    }

}

package com.maxback.common;

import com.maxback.common.model.WordPojo;
import com.maxback.common.repository.WordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.RestTemplate;

import java.util.Comparator;
import java.util.List;
import java.util.Random;


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
                .sorted(Comparator.comparing(WordPojo::getWord))
                .forEach(w -> System.out.println(w.getWord() + " x " + w.getCounter()));

        log.info("initialize word length...");
        restTemplate.postForEntity(dictApiUrl + "/1", null, String.class);
    }

    @Scheduled(fixedRateString = "60000") // one minute
    public void fetch() {
        for (int i = 1; i <= 5; i++) {
            String word = getRandomWord(i);
            log.info("trying to fetch \"" + word + "\"...");
            getWord(word);
        }
    }

    public String getWord(String word) {
        WordPojo wordPojo = wordRepository.findByWord(word);
        if(wordPojo!=null) {
            if(wordPojo.getCounter() > 0) {
                wordPojo.setCounter(wordPojo.getCounter() - 1); // TODO BUILDER
                wordRepository.save(wordPojo);
                log.info("\"" + word + "\" fetched and counter decreased");
            }else{
                log.info("we are out of \"" + word + "\" in the store, need to order");
            }
        }else{
            log.info("\"" + word + "\" not exist");
        }

        return word;
    }

    public String getRandomWord(int length) {
        Random r = new Random();
        int asciiNum;
        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < length; i++) {
            asciiNum = r.nextInt(97, 123);
            sb.append((char) asciiNum);
        }

        return sb.toString();
    }

}

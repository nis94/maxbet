package com.maxback.common.service;

import com.maxback.common.model.DictionaryWordData;
import com.maxback.common.model.WordPojo;
import com.maxback.common.repository.WordRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class ClassifyingService {

    private final WordRepository wordRepository;
    private final RestTemplate restTemplate;

    @Value("${dictionary.api.url}")
    private String dictApiUrl;


    public ClassifyingService(WordRepository wordRepository, RestTemplate restTemplate) {
        this.wordRepository = wordRepository;
        this.restTemplate = restTemplate;

    }

    public void analyzeWord(DictionaryWordData entry) {
        String newWord = entry.getWord();
        WordPojo wordFromDb = wordRepository.findByWord(newWord);
        if (wordFromDb != null) {
            log.info("\""+ newWord + "\" already exist, updating counter");
            wordRepository.save(new WordPojo(wordFromDb));
        } else {
            wordRepository.save(
                    new WordPojo(newWord, entry.getDefinition(), entry.getCreationDate())
            );
            log.info("Saved new word -> \"" + newWord + "\"");
        }
        int newLength = newWord.length() + 1;
        restTemplate.postForEntity(dictApiUrl + "/" + newLength, null, String.class);
        log.info("Sent new length to dictionary service: " + newLength);
    }

}

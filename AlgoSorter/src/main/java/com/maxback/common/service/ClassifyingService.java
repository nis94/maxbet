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
        int newWordLength = newWord.length();
        WordPojo wordFromDb = wordRepository.findByLength(newWordLength);
        if (wordFromDb != null) {
            log.info("Word with length " + newWordLength + " already exist -> \"" + wordFromDb.getWord() + "\"");
        } else {
            wordRepository.save(
                    new WordPojo(newWordLength, newWord, entry.getDefinition(), entry.getCreationDate())
            );
            log.info("Saved new word -> \"" + newWord + "\" (length = " + newWordLength + ")");
        }
        restTemplate.postForEntity(dictApiUrl + "/" + (newWordLength + 1), null, String.class);
        log.info("Sent new length to dictionary service: " + (newWordLength + 1));
    }

}

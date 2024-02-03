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
            log.debug("\"" + newWord + "\" already exist, updating counter");
            wordFromDb.setCounter(wordFromDb.getCounter() + 1);
            wordRepository.save(wordFromDb);
        } else {
            log.debug("Saving new word -> \"" + newWord + "\"");
            wordRepository.save(
                    new WordPojo(newWord, entry.getDefinition(), entry.getCreationDate())
            );
        }

        int newLength = newWord.length() + 1;
        restTemplate.postForEntity(dictApiUrl + "/" + newLength, null, String.class);
        log.debug("Dictionary service length set to : " + newLength);
    }

    public String buyWord(String word) {
        WordPojo wordPojo = wordRepository.findByWord(word);
        if(wordPojo!=null) {
            if(wordPojo.getCounter() > 0) {
                wordPojo.setCounter(wordPojo.getCounter() - 1);
                wordRepository.save(wordPojo);
                log.info("[" + word + "] Sold");
            }else{
                log.info("we are out of [" + word + "]");
            }
        }else{
            log.debug("\"" + word + "\" not exist");
        }

        return word;
    }

}

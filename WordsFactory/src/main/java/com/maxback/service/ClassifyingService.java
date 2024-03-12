package com.maxback.service;

import com.maxback.common.model.DictionaryWordData;
import com.maxback.common.model.WordPojo;
import com.maxback.model.StorageJob;
import com.maxback.repository.WordsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ClassifyingService {

    private final WordsRepository wordsRepository;
    private final DictionaryService dictionaryService;


    public ClassifyingService(WordsRepository wordsRepository, DictionaryService dictionaryService) {
        this.wordsRepository = wordsRepository;
        this.dictionaryService = dictionaryService;
    }

    public void analyzeWord(DictionaryWordData entry) {
        String newWord = entry.getWord();
        WordPojo wordFromDb = wordsRepository.findByWord(newWord);

        if (wordFromDb != null) {
            log.info("\"" + newWord + "\" already exist, updating counter");
            wordFromDb.setCounter(wordFromDb.getCounter() + 1);
            wordsRepository.save(wordFromDb);
        } else {
            log.info("Saving new word -> \"" + newWord + "\"");
            wordsRepository.save(
                    new WordPojo(newWord, entry.getDefinition(), entry.getCreationDate())
            );
        }
    }

    public String buyWord(String word) {
        WordPojo wordPojo = wordsRepository.findByWord(word);
        if(wordPojo!=null) {
            if(wordPojo.getCounter() > 0) {
                wordPojo.setCounter(wordPojo.getCounter() - 1);
                wordsRepository.save(wordPojo);
                log.info("[" + word + "] Sold");
            }else{
                log.info("we are out of [" + word + "]");
            }
        }else{
            log.info("\"" + word + "\" not exist");
        }

        return word;
    }

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void consume(StorageJob job) {
        log.info(String.format("Got message -> \"%s\"", job));
        DictionaryWordData wordData = dictionaryService.getDictionaryWordData(job);
        log.info(String.format("Word Definition -> \"%s\"", wordData.getDefinition()));
        analyzeWord(wordData);
        log.info(String.format("Job ID -> %s", job.getJobId()));
    }

}

package com.maxback.service;

import com.maxback.common.model.DictionaryWordData;
import com.maxback.common.model.WordPojo;
import com.maxback.model.StorageJob;
import com.maxback.repository.JobsRepository;
import com.maxback.repository.WordsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ClassifyingService {

    private final WordsRepository wordsRepository;
    private final JobsRepository jobsRepository;
    private final DictionaryService dictionaryService;


    public ClassifyingService(WordsRepository wordsRepository, DictionaryService dictionaryService,
                              JobsRepository jobsRepository) {
        this.wordsRepository = wordsRepository;
        this.dictionaryService = dictionaryService;
        this.jobsRepository = jobsRepository;
    }

    public void analyzeWord(DictionaryWordData entry) {
        String newWord = entry.getWord();
        WordPojo wordFromDb = wordsRepository.findByWord(newWord);

        if (wordFromDb != null) {
            log.info("[" + newWord + "] already exist, updating counter");
            wordFromDb.setCounter(wordFromDb.getCounter() + 1);
            wordsRepository.save(wordFromDb);
        } else {
            log.info("Saving new word -> [" + newWord + "]");
            wordsRepository.save(
                    new WordPojo(newWord, entry.getDefinition(), entry.getCreationDate())
            );
        }
    }

//    public String buyWord(String word) {
//        WordPojo wordPojo = wordsRepository.findByWord(word);
//        if(wordPojo!=null) {
//            if(wordPojo.getCounter() > 0) {
//                wordPojo.setCounter(wordPojo.getCounter() - 1);
//                wordsRepository.save(wordPojo);
//                log.info("[" + word + "] Sold");
//            }else{
//                log.info("we are out of [" + word + "]");
//            }
//        }else{
//            log.info("\"" + word + "\" not exist");
//        }
//
//        return word;
//    }

    @RabbitListener(queues = "${rabbitmq.queue.name}")
    public void consume(StorageJob job) {
        log.info("Got message -> [" + job + "]");
        DictionaryWordData wordData = dictionaryService.getDictionaryWordData(job);
        if (wordData != null) {
            log.info("Word Definition -> [" + wordData.getDefinition() + "]");
            analyzeWord(wordData);
        }
        log.info(String.format("Job ID -> %s", job.getJobId()));
    }

    @Scheduled(fixedRateString = "PT5M")
    public void reconcile() {
        List<StorageJob> pendingJobs = jobsRepository.findByStatus("PENDING");
        pendingJobs.forEach(j -> {
            consume(j);
            log.info("Reconciled -> [" + j.getPayload() + "]");
        });
    }


}

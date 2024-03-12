package com.maxback.service;

import com.maxback.common.model.WordPojo;
import com.maxback.model.StorageJob;
import com.maxback.repository.JobsRepository;
import com.maxback.repository.WordsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class WordsService {

    private final WordsRepository wordsRepository;
    private final JobsRepository jobsRepository;
    private final RabbitMQProducer rabbitProducer;

    public WordsService(WordsRepository wordsRepository, JobsRepository jobsRepository, RabbitMQProducer rabbitProducer) {
        this.wordsRepository = wordsRepository;
        this.jobsRepository = jobsRepository;
        this.rabbitProducer = rabbitProducer;
    }

//    @Timed(value = "word.gen.time", description = "Time taken to generate a word")
//    @Counted(value = "total.words.generated", description = "amount of words generated")
//    public void generateRandomWord(int length) {
//        Random r = new Random();
//        int asciiNum;
//        boolean isSucceeded;
//        StringBuilder sb;
//
//        do {
//            sb = new StringBuilder();
//            for (int i = 0; i < length; i++) {
//                asciiNum = r.nextInt(97, 123);
//                sb.append((char) asciiNum);
//            }
//            isSucceeded = qualityCheck(sb.toString());
//        } while (!isSucceeded);
//    }

    public String generateSpecificWord(String word) {
        StorageJob job = new StorageJob(UUID.randomUUID().toString(), "QUEUED", Instant.now().toEpochMilli(), word);
        jobsRepository.save(job);
        rabbitProducer.sendMessage(job);

        return job.getJobId();
    }

    public List<WordPojo> getAll() {
        return wordsRepository.findAll();
    }
}
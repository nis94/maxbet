package com.maxback.service;

import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class WordsService {

    @Timed(value = "word.gen.time", description = "Time taken to generate a word")
    @Counted(value = "total.words.generated", description = "amount of words generated")
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
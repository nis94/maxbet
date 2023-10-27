package com.maxback.service;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class WordsService {

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
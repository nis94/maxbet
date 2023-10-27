package com.maxback.controller;

import com.maxback.service.WordsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class WordsController {

    private final WordsService wordsService;

    @Autowired
    public WordsController(WordsService service) {
        wordsService = service;
    }

    @GetMapping("/words/{length}")
    private String getRandomWord(@PathVariable int length) {
        String word = wordsService.getRandomWord(length);
        log.info("generated the word: \"" + word + "\"");

        return word;
    }

}

package com.maxback.controller;

import com.maxback.common.model.WordPojo;
import com.maxback.service.WordsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
public class WordsController {

    private final WordsService wordsService;

    public WordsController(WordsService service) {
        wordsService = service;
    }

//    @GetMapping("/new/{length}")
//    private HttpStatus storeRandomWord(@PathVariable int length) {
//        wordsService.generateRandomWord(length);
//        log.info("stored new word");
//
//        return HttpStatus.OK;
//    }

    @GetMapping("/qa/{word}")
    private HttpStatus storeSpecificWord(@PathVariable String word) {
        String jobId = wordsService.generateSpecificWord(word);
        log.info("sent job: " + jobId + " with the word: \"" + word + "\"");

        return HttpStatus.OK;
    }

    @GetMapping("/words/all")
    private List<WordPojo> getAll() {

        return wordsService.getAll();
    }
}
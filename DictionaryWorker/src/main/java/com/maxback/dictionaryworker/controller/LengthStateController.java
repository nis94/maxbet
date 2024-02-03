package com.maxback.dictionaryworker.controller;

import com.maxback.dictionaryworker.service.WordService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class LengthStateController {

    private final WordService wordService;

    public LengthStateController(WordService service) {
        wordService = service;
    }

    @GetMapping("/length")
    private int getLengthState() {
        return wordService.getLengthState();
    }

    @PostMapping("/length/{length}")
    private int setLengthState(@PathVariable int length) {
        wordService.setLengthState(length);
        log.info("wordService length state set to: " + wordService.getLengthState());

        return length;
    }

}

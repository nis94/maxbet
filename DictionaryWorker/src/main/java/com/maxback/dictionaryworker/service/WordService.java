package com.maxback.dictionaryworker.service;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class WordService {

    private final RestTemplate restTemplate;
    private final String wordsApiUrl;

    @Getter
    @Setter
    private int lengthState = 3;

    public WordService(RestTemplate restTemplate, String wordsApiUrl) {
        this.restTemplate = restTemplate;
        this.wordsApiUrl = wordsApiUrl;
    }

    public String fetchRandomWordFromApi() {
        log.debug("post to: [" + wordsApiUrl + "/" + lengthState + "]");

        ResponseEntity<String> response =
                restTemplate.getForEntity(wordsApiUrl + "/" + lengthState, String.class);

        return response.getBody();
    }

}

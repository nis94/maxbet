package com.maxback.dictionaryworker;

import com.maxback.common.model.DictionaryWordData;
import com.maxback.dictionaryworker.service.DictionaryService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.mock;

@Slf4j
@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // TODO - WHAT IS THAT & WHY - "NONE" ?
class DictionaryWorkerApplicationTests {

    private RestTemplate restTemplate;
    private DictionaryService dictionaryService;
    private final String DICTIONARY_URL = "https://api.dictionaryapi.dev/api/v2/entries/en/";


    @Test
    void whenExistInDictionary_thenCreateWordObject() {

        //Arrange
        restTemplate = mock(RestTemplate.class);
        dictionaryService = new DictionaryService(restTemplate);
        String testWord = "test";

        String TEST_DEFINITION = "[{\"word\": \"test\", \"meanings\": [{\"definitions\": [{\"definition\": \"A challenge, trial.\"}]}]}\n]";
        Mockito.when(restTemplate.getForEntity(DICTIONARY_URL + testWord, String.class))
                .thenReturn(new ResponseEntity<>(TEST_DEFINITION, HttpStatus.OK));
        //Act
        DictionaryWordData wordObj = dictionaryService.getDictionaryWordData(testWord);

        //Assert
        Assertions.assertEquals(testWord, wordObj.getWord());
        Assertions.assertEquals("A challenge, trial.", wordObj.getDefinition());

    }

    @Test
    void whenNotExistInDictionary_thenReturnNull() {

        //Arrange
        restTemplate = mock(RestTemplate.class);
        dictionaryService = new DictionaryService(restTemplate);
        String testWord = "lkfsn";

        Mockito.when(restTemplate.getForEntity(DICTIONARY_URL + testWord, String.class))
                .thenThrow(HttpClientErrorException.NotFound.class);
        //Act
        DictionaryWordData wordObj = dictionaryService.getDictionaryWordData(testWord);

        //Assert
        Assertions.assertNull(wordObj);

    }

    @Test
    void whenTooManyRequest_thenWhat() {

        //Arrange
        restTemplate = mock(RestTemplate.class);
        dictionaryService = new DictionaryService(restTemplate);
        String testWord = "lkfsn";

        Mockito.when(restTemplate.getForEntity(DICTIONARY_URL + testWord, String.class))
                .thenThrow(HttpClientErrorException.TooManyRequests.class);

        //Act
        DictionaryWordData wordObj = dictionaryService.getDictionaryWordData(testWord);

        //Assert
        Assertions.assertNull(wordObj);

    }

}
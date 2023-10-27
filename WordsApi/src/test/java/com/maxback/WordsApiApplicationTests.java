package com.maxback;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.internal.matchers.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.util.Objects;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WordsApiApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;


    @Test
    public void whenCallingWordEndPoint_thenOnlyLetters() {

        ResponseEntity<String> response =
                restTemplate.getForEntity("http://localhost:" + port + "/words/100", String.class);

        String word = response.getBody();
        assert (word != null);
        Assertions.assertEquals(100, word.length());
        Assertions.assertTrue(isContainOnlyLetters(Objects.requireNonNull(word)));
    }

    private boolean isContainOnlyLetters(String word) {
        boolean result = true;
        char currChar;

        for (int i = 0; i < word.length(); i++) {
            currChar = word.charAt(i);

            if(currChar < 'a' || currChar > 'z'){
                result = false;
                break;
            }
        }

        return result;
    }

}

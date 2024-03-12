package com.maxback;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

@Disabled
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WordsFactoryApplicationTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;


    @Test
    public void whenCallingRandomWordEndPoint_thenSave() {

        ResponseEntity<String> response =
                restTemplate.getForEntity("http://localhost:" + port + "/words/100", String.class);

        String word = response.getBody();
        assert (word != null);
        Assertions.assertEquals(100, word.length());
    }

    @Test
    public void whenCallingExistingWordEndPoint_thenSave() {

        ResponseEntity<String> response =
                restTemplate.getForEntity("http://localhost:" + port + "/words/100", String.class);

        String word = response.getBody();
        assert (word != null);
        Assertions.assertEquals(100, word.length());
    }

    @Test
    public void whenWordNotExist_thenDontSave() {

        ResponseEntity<String> response =
                restTemplate.getForEntity("http://localhost:" + port + "/words/100", String.class);

        String word = response.getBody();
        assert (word != null);
        Assertions.assertEquals(100, word.length());
    }


}

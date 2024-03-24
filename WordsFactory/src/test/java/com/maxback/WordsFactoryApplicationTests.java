package com.maxback;

import com.maxback.container.AbstractWordsFactoryTestContainers;
import com.maxback.repository.JobsRepository;
import com.maxback.repository.WordsRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.TimeUnit;

import static org.testcontainers.shaded.org.awaitility.Awaitility.waitAtMost;
import static org.testcontainers.shaded.org.awaitility.pollinterval.FibonacciPollInterval.fibonacci;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WordsFactoryApplicationTests extends AbstractWordsFactoryTestContainers {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private WordsRepository wordsRepository;

    @Autowired
    private JobsRepository jobsRepository;

    @LocalServerPort
    private int port;


    @Test
    public void whenCallingRandomWordEndPoint_thenSave() {

        ResponseEntity<String> response =
                restTemplate.getForEntity("http://localhost:" + port + "/qa/test", String.class);

        waitAtMost(10, TimeUnit.SECONDS)
                .with().pollInterval(fibonacci(TimeUnit.SECONDS)).await()
                .untilAsserted(
                        () -> {
                            Assertions.assertTrue(response.getStatusCode().is2xxSuccessful());
                            Assertions.assertEquals(1, jobsRepository.findAll().size());
                            Assertions.assertEquals(1, wordsRepository.findAll().size());
                        });
    }


}

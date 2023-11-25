package com.maxback.common;

import com.maxback.common.model.DictionaryWordData;
import com.maxback.common.model.WordPojo;
import com.maxback.common.repository.WordRepository;
import com.maxback.common.service.ClassifyingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.mock;

@SpringBootTest
class AlgoSorterApplicationTest {

    @Autowired
    private WordRepository wordRepository;


    @Test
    void whenNewWord_thenSave(){
        // Arrange
        RestTemplate restTemplate = mock(RestTemplate.class);
        ClassifyingService classifyingService = new ClassifyingService(wordRepository, restTemplate);

        // Act
        classifyingService.analyzeWord(new DictionaryWordData("word", "definition", 12345L));

        // Assert
        WordPojo wordFromDb = wordRepository.findByWord("word");

        Assertions.assertNotNull(wordFromDb);
        Assertions.assertEquals("word", wordFromDb.getWord());
        Assertions.assertEquals("definition", wordFromDb.getDefinition());
        Assertions.assertEquals(12345L, wordFromDb.getCreationDate());
        Assertions.assertEquals(1L, wordFromDb.getCounter());

    }

    @Test
    void whenExistingWord_thenSave(){
        // Arrange
        wordRepository.save(new WordPojo("word", "definition", 12345L));
        RestTemplate restTemplate = mock(RestTemplate.class);
        ClassifyingService classifyingService = new ClassifyingService(wordRepository, restTemplate);

        // Act
        classifyingService.analyzeWord(new DictionaryWordData("word", "definition", 222222L));

        // Assert
        WordPojo wordFromDb = wordRepository.findByWord("word");

        Assertions.assertNotNull(wordFromDb);
        Assertions.assertEquals("word", wordFromDb.getWord());
        Assertions.assertEquals("definition", wordFromDb.getDefinition());
        Assertions.assertEquals(12345L, wordFromDb.getCreationDate());
        Assertions.assertEquals(2L, wordFromDb.getCounter());
    }

    // TODO: TOO LONG DEFINITION:
    //{"word":"taco","definition":"A Mexican snack food; a small tortilla (soft or hard shelled), with typically some type of meat, rice, beans, cheese, diced vegetables (usually tomatoes and lettuce, as served in the United States, and cilantro, onion, and avocado, as served in México) and salsa.","creationDate":1700928480706}

}

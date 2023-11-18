package com.maxback.common.model;

//TODO - make it common!

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@Entity
@Table(name = "words")
public class WordPojo implements Serializable {
    @Id
    private String word;
    private String definition;
    private long creationDate;
    private long counter;

    public WordPojo(String word,String definition, long creationDate){
        this.word = word;
        this.definition = definition;
        this.creationDate = creationDate;
        this.counter = 1;
    }

    public WordPojo(WordPojo existingWord) {
        this.word = existingWord.word;
        this.definition = existingWord.definition;
        this.creationDate = existingWord.creationDate;
        this.counter = existingWord.counter + 1;
    }
}

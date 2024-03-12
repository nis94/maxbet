package com.maxback.common.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "words")
public class WordPojo implements Serializable {
    @Id
    private String word;
    private String definition;
    private long creationDate;
    private double price;
    private long counter;

    public WordPojo(String word, String definition, long creationDate){
        this.word = word;
        this.definition = definition;
        this.creationDate = creationDate;
        this.price = calcPrice(word);
        this.counter = 1;
    }

    private double calcPrice(String word) {
        AtomicInteger res = new AtomicInteger();
        word.chars().forEach(ch -> res.set(res.get() + ch));

        return res.get();
    }

}

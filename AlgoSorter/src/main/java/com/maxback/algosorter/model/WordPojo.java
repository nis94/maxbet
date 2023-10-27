package com.maxback.algosorter.model;

//TODO - make it common!

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "word")
public class WordPojo implements Serializable {
    @Id
    private int length;
    private String word;
    private String definition;
    private long creationDate;
}

package com.maxback.algosorter.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class DictionaryEntry implements Serializable {
    private String word;
    private String definition;
    private long creationDate;
}

//TODO - make it common!

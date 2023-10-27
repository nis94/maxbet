package com.maxback.dictionaryworker.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DictionaryEntry {
    private String word;
    private String definition;
    private long creationDate;
}

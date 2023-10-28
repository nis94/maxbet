package com.maxback.common.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class PokemonData implements Serializable {
    private String name;
    private List<String> types;
    private int height;
    private int weight;
    private long creationDate;
}

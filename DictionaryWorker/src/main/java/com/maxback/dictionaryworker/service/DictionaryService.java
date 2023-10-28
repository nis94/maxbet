package com.maxback.dictionaryworker.service;

import com.maxback.common.model.DictionaryWordData;
import com.maxback.common.model.PokemonData;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class DictionaryService {

    private final RestTemplate restTemplate;


    public DictionaryService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public DictionaryWordData getDictionaryWordData(String word) {
        String definition = getWordDefinition(word);

        return definition.isEmpty() ?
                null :
                new DictionaryWordData(word, definition, Instant.now().toEpochMilli());
    }

    private String getWordDefinition(String word) {
        String definition = "";

        try {
            ResponseEntity<String> response =
                    restTemplate.getForEntity("https://api.dictionaryapi.dev/api/v2/entries/en/" + word, String.class);

            JSONObject responseJson = new JSONArray(response.getBody()).getJSONObject(0);
            JSONArray ja = responseJson.getJSONArray("meanings");
            JSONArray definitions = ((JSONObject) ja.get(0)).getJSONArray("definitions");
            definition = definitions.getJSONObject(0).getString("definition");
        } catch (HttpClientErrorException.TooManyRequests e) {
            log.warn("Too many requests - will be sent to retry queue");
        }catch (HttpClientErrorException.NotFound e) {
            log.debug("Couldn't find \"" + word + "\" in dictionary");
        } catch (JSONException e) {
            log.error("Failed to parse response JSON", e);
        }

        return definition;
    }


    public PokemonData getPokemonData(String word) {
        PokemonData pokemonData = null;

        try {
            ResponseEntity<String> response =
                    restTemplate.getForEntity("https://pokeapi.co/api/v2/pokemon/" + word, String.class);

            JSONObject responseJson = new JSONObject(response.getBody());
            JSONArray typesJson = responseJson.getJSONArray("types");
            String hight = responseJson.getString("height");
            String weight = responseJson.getString("weight");
            List<String> types = new ArrayList<>();
            typesJson.forEach( t -> {
                String type = (((JSONObject)t).getJSONObject("type")).getString("name");
                types.add(type);
            });
            pokemonData = new PokemonData(word, types, Integer.parseInt(hight), Integer.parseInt(weight), Instant.now().toEpochMilli() );
        } catch (HttpClientErrorException.TooManyRequests e) {
            log.warn("Too many requests - will be sent to retry queue");
        }catch (HttpClientErrorException.NotFound e) {
            log.debug("Couldn't find \"" + word + "\" in dictionary");
        } catch (JSONException e) {
            log.error("Failed to parse response JSON", e);
        }

        return pokemonData;
    }

}
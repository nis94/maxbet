package com.maxback.service;

import com.maxback.common.model.DictionaryWordData;
import com.maxback.model.StorageJob;
import com.maxback.repository.JobsRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;


@Service
@Slf4j
public class DictionaryService {

    private final RestTemplate restTemplate;
    private final JobsRepository jobsRepository;

    public DictionaryService(RestTemplate restTemplate, JobsRepository jobsRepository) {
        this.restTemplate = restTemplate;
        this.jobsRepository = jobsRepository;
    }

    public DictionaryWordData getDictionaryWordData(StorageJob job) {
        String definition = getWordDefinition(job);

        if (definition.isEmpty()) {
            jobsRepository.save(new StorageJob(job.getJobId(), "NOT-FOUND", job.getStartTime(), job.getPayload()));
            return null;
        } else {
            if (job.getPayload().equals(("test"))) { //TODO - remove after test
                jobsRepository.save(new StorageJob(job.getJobId(), "PENDING", job.getStartTime(), job.getPayload()));
            }
            else {
                jobsRepository.save(new StorageJob(job.getJobId(), "DONE", job.getStartTime(), job.getPayload()));
            }
            return new DictionaryWordData(job.getPayload(), definition, Instant.now().toEpochMilli());
        }
    }

    @SneakyThrows
    private String getWordDefinition(StorageJob job) {
        String word = job.getPayload();
        String definition = "";

        try {
            ResponseEntity<String> response =
                    restTemplate.getForEntity("https://api.dictionaryapi.dev/api/v2/entries/en/" + word, String.class);

            JSONObject responseJson = new JSONArray(response.getBody()).getJSONObject(0);
            JSONArray ja = responseJson.getJSONArray("meanings");
            JSONArray definitions = ((JSONObject) ja.get(0)).getJSONArray("definitions");
            definition = definitions.getJSONObject(0).getString("definition");
        } catch (HttpClientErrorException.TooManyRequests e) {
            log.warn("Too many requests");
            jobsRepository.save(new StorageJob(job.getJobId(), "PENDING", job.getStartTime(), job.getPayload()));
        }catch (HttpClientErrorException.NotFound e) {
            log.info("Couldn't find \"" + word + "\" in dictionary");
            jobsRepository.save(new StorageJob(job.getJobId(), "NOT-FOUND", job.getStartTime(), job.getPayload()));
        } catch (JSONException e) {
            log.error("Failed to parse response JSON", e);
            jobsRepository.save(new StorageJob(job.getJobId(), "FAILED", job.getStartTime(), job.getPayload()));
        }

        return definition;
    }

}
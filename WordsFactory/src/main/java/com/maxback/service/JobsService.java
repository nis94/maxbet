package com.maxback.service;

import com.maxback.model.StorageJob;
import com.maxback.repository.JobsRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class JobsService {

    private final JobsRepository jobsRepository;

    public JobsService(JobsRepository jobsRepository) {
        this.jobsRepository = jobsRepository;
    }

    public List<StorageJob> getAll() {
        return this.jobsRepository.findAll();
    }
}
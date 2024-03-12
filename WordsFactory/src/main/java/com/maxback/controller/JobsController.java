package com.maxback.controller;

import com.maxback.model.StorageJob;
import com.maxback.service.JobsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@Slf4j
@RestController
public class JobsController {

    private final JobsService jobsService;

    public JobsController(JobsService service) {
        jobsService = service;
    }

    @GetMapping("/jobs/all")
    private List<StorageJob> getAll() {

        return jobsService.getAll();
    }
}
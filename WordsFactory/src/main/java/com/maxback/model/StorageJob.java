package com.maxback.model;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class StorageJob implements Serializable {
    @Id
    private String jobId;
    private String status;
    private long startTime;
    private String payload;
}

package com.maxback.repository;

import com.maxback.model.StorageJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JobsRepository extends JpaRepository<StorageJob,String> {
    List<StorageJob> findByStatus(String status);
}
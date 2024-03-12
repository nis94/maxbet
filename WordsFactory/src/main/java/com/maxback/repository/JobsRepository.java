package com.maxback.repository;

import com.maxback.model.StorageJob;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobsRepository extends JpaRepository<StorageJob,String> {
}
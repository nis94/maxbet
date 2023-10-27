package com.maxback.algosorter.repository;

import com.maxback.algosorter.model.WordPojo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordRepository extends JpaRepository<WordPojo,String> {

    WordPojo findByLength(int length);

}
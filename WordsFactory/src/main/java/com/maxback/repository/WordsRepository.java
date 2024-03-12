package com.maxback.repository;

import com.maxback.common.model.WordPojo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordsRepository extends JpaRepository<WordPojo,String> {
    WordPojo findByWord(String word);
}
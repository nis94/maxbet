package com.maxback.common.repository;

import com.maxback.common.model.WordPojo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WordRepository extends JpaRepository<WordPojo,String> {

    WordPojo findByLength(int length);

}
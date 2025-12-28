package com.example.hotelbackend.repository;

import com.example.hotelbackend.model.BlogHomeContent;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface BlogHomeRepository
        extends MongoRepository<BlogHomeContent, String> {

    Optional<BlogHomeContent> findFirstBy();
}


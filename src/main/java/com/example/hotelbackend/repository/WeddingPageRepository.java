package com.example.hotelbackend.repository;

import com.example.hotelbackend.model.WeddingPage;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WeddingPageRepository extends MongoRepository<WeddingPage, String> {

}


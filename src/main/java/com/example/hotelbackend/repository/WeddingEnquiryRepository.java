package com.example.hotelbackend.repository;

import com.example.hotelbackend.model.WeddingEnquiry;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface WeddingEnquiryRepository
        extends MongoRepository<WeddingEnquiry, String> {
}


package com.example.hotelbackend.repository;

import com.example.hotelbackend.model.EventEnquiry;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EventEnquiryRepository
        extends MongoRepository<EventEnquiry, String> {
}


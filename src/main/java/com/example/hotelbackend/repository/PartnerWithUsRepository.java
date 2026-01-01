package com.example.hotelbackend.repository;

import com.example.hotelbackend.model.PartnerWithUsEnquiry;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PartnerWithUsRepository
        extends MongoRepository<PartnerWithUsEnquiry, String> {
}


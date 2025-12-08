package com.example.hotelbackend.repository;

import com.example.hotelbackend.model.OffersPageContent;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface OffersPageRepository extends MongoRepository<OffersPageContent, String> {
    Optional<OffersPageContent> findFirstBy();
}


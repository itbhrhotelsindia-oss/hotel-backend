package com.example.hotelbackend.repository;

import com.example.hotelbackend.model.HotelDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface HotelDetailsRepository
        extends MongoRepository<HotelDetails, String> {
    List<HotelDetails> findByStatus(String status);
}


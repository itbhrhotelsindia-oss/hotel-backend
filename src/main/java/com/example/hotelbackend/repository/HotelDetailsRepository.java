package com.example.hotelbackend.repository;

import com.example.hotelbackend.model.HotelDetails;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HotelDetailsRepository
        extends MongoRepository<HotelDetails, String> {
}


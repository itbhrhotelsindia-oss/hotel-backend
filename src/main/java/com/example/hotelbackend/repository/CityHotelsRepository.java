package com.example.hotelbackend.repository;

import com.example.hotelbackend.model.CityHotels;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CityHotelsRepository extends MongoRepository<CityHotels, String> {
}

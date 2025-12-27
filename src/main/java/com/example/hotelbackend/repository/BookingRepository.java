package com.example.hotelbackend.repository;

import com.example.hotelbackend.model.Booking;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface BookingRepository extends MongoRepository<Booking, String> {
}


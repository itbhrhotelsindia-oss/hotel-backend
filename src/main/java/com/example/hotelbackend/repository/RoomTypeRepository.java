package com.example.hotelbackend.repository;

import com.example.hotelbackend.model.RoomType;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface RoomTypeRepository extends MongoRepository<RoomType, String> {

    List<RoomType> findByHotelIdAndIsActiveTrue(String hotelId);

    boolean existsByHotelIdAndNameIgnoreCase(String hotelId, String name);
}

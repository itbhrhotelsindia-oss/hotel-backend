package com.example.hotelbackend.repository;

import com.example.hotelbackend.model.RoomInventory;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomInventoryRepository extends MongoRepository<RoomInventory, String> {

    /* =========================
       OWNER / ADMIN QUERIES
       ========================= */

    // Get inventory for a room type between dates
    List<RoomInventory> findByHotelIdAndRoomTypeIdAndDateBetween(
            String hotelId,
            String roomTypeId,
            LocalDate startDate,
            LocalDate endDate
    );

    // Get inventory for a specific date
    Optional<RoomInventory> findByHotelIdAndRoomTypeIdAndDate(
            String hotelId,
            String roomTypeId,
            LocalDate date
    );

    /* =========================
       BOOKING QUERIES
       ========================= */

    // Check availability (used before booking)
    List<RoomInventory> findByHotelIdAndRoomTypeIdAndDateBetweenAndActiveTrueAndAvailableRoomsGreaterThan(
            String hotelId,
            String roomTypeId,
            LocalDate startDate,
            LocalDate endDate,
            int availableRooms
    );
}


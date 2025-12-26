package com.example.hotelbackend.repository;

import com.example.hotelbackend.model.RoomInventory;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface RoomInventoryRepository extends MongoRepository<RoomInventory, String> {

    /* =========================================================
       OWNER / ADMIN QUERIES (SAFE)
       ========================================================= */

    // Get inventory for a room type between dates (ADMIN calendar view)
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

    /* =========================================================
       BOOKING QUERY (STEP 6 — AVAILABILITY CHECK)
       ========================================================= */

    /**
     * Finds available inventory for booking.
     * - date >= checkIn
     * - date < checkOut (exclusive)
     * - active = true
     * - availableRooms > required
     *
     * IMPORTANT:
     * Uses @Query to avoid MongoDB duplicate date criteria bug.
     */
    @Query("""
    {
      hotelId: ?0,
      roomTypeId: ?1,
      date: { $gte: ?2, $lt: ?3 },
      active: true,
      availableRooms: { $gt: ?4 }
    }
    """)
    List<RoomInventory> findAvailableInventory(
            String hotelId,
            String roomTypeId,
            LocalDate checkIn,
            LocalDate checkOut,
            int minAvailableRooms
    );
}

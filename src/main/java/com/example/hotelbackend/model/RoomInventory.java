package com.example.hotelbackend.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "room_inventory")
public class RoomInventory {

    @Id
    private String id;

    // 🔑 References
    private String hotelId;        // HOTEL-JIMCORBETT-001
    private String roomTypeId;     // RoomType._id

    // 📅 Inventory is PER DAY
    private String date;

    // 🏨 Capacity
    private int totalRooms;
    private int availableRooms;

    // 💰 Pricing
    private double pricePerNight;

    // 🔒 Control
    private boolean active;        // true = open for booking, false = blocked
}


package com.example.hotelbackend.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "room_types")
public class RoomType {

    @Id
    private String id;

    private String hotelId;

    private String name;
    private String description;
    private int maxAdults;     // ✅ NEW
    private int maxChildren;
    private int maxGuests;

    private double basePrice;

    /** 🔥 NEW: flat prices per room per night */
    private double breakfastPrice;            // e.g. 500
    private double lunchDinnerPrice;           // e.g. 1200

    /** 🔥 NEW: percentage markup for pay-at-hotel */
    private double payAtHotelMarkupPercent;

    private boolean isActive;

    private Instant createdAt;
}

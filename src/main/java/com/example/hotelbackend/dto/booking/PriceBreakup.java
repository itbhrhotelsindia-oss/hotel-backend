package com.example.hotelbackend.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PriceBreakup {
    private String type;          // ROOM_ONLY, ROOM_WITH_BREAKFAST, ROOM_WITH_MEALS
    private String payMode;
    private double pricePerNight;
    private double totalAmount;
}


package com.example.hotelbackend.dto.roomtype;

import lombok.Data;

import java.util.List;

@Data
public class UpdateRoomTypeRequest {

    private String name;
    private String description;
    private int maxAdults;     // ✅
    private int maxChildren;
    private double basePrice;
    private List<String> images;
    private List<String> amenities;

    private double breakfastPrice;
    private double lunchDinnerPrice;
    private double payAtHotelMarkupPercent;
}

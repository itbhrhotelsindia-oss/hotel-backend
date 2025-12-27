package com.example.hotelbackend.dto.owner;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OwnerHotelResponse {
    private String hotelId;
    private String hotelName;
    private String city;
}


package com.example.hotelbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HotelLookupResponse {

    private String cityId;
    private String cityName;

    private String hotelId;
    private String hotelName;
}


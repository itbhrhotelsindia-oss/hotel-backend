package com.example.hotelbackend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Hotel {

    private String hotelId;
    private String city;
    private String name;
    private String address;
    private String imageUrl;

    private List<String> services; // ✅ NEW FIELD (MICE, Wedding, Vacation)
}

package com.example.hotelbackend.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document("city_hotels")
public class CityHotels {

    @Id
    private String id;

    private String name;    // Jim Corbett / Rishikesh
    private String state;   // Uttarakhand
    private List<Hotel> hotels;  // List of hotels
}


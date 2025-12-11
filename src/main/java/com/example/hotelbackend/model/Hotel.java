package com.example.hotelbackend.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Hotel {
    private String city;
    private String name;
    private String address;
    private String imageUrl;
}

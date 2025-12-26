package com.example.hotelbackend.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PriceBreakup {
    private String date;
    private double price;
}


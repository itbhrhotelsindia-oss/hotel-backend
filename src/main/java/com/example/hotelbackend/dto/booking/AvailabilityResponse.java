package com.example.hotelbackend.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AvailabilityResponse {

    private boolean available;

    private String hotelId;
    private String roomTypeId;

    private int nights;
    private int roomsRequested;

    private long totalAmount;

    private List<PriceBreakup> breakup;
}


package com.example.hotelbackend.dto.booking;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AvailabilityFailureResponse {

    private boolean available;
    private String reason;
    private String failedDate;
}


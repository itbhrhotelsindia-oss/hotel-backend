package com.example.hotelbackend.controller;

import com.example.hotelbackend.dto.booking.CheckAvailabilityRequest;
import com.example.hotelbackend.service.BookingAvailabilityService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/availability")
public class AvailabilityController {

    private final BookingAvailabilityService availabilityService;

    public AvailabilityController(BookingAvailabilityService availabilityService) {
        this.availabilityService = availabilityService;
    }

    /**
     * STEP 6 — Check room availability (READ-ONLY)
     */
    @PostMapping
    public ResponseEntity<?> checkAvailability(
            @RequestBody CheckAvailabilityRequest request
    ) {
        return ResponseEntity.ok(
                availabilityService.checkAvailability(request)
        );
    }
}


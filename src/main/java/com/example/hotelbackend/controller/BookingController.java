package com.example.hotelbackend.controller;

import com.example.hotelbackend.dto.booking.CreateBookingRequest;
import com.example.hotelbackend.model.Booking;
import com.example.hotelbackend.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/public/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    /**
     * STEP 8 — Create booking (PENDING)
     */
    @PostMapping
    public ResponseEntity<Booking> createBooking(
            @RequestBody CreateBookingRequest request
    ) {
        return ResponseEntity.ok(
                bookingService.createPendingBooking(request)
        );
    }
}


package com.example.hotelbackend.controller;

import com.example.hotelbackend.model.Booking;
import com.example.hotelbackend.repository.BookingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/bookings")
public class AdminBookingController {

    private final BookingRepository bookingRepo;

    public AdminBookingController(BookingRepository bookingRepo) {
        this.bookingRepo = bookingRepo;
    }

    /**
     * GET /api/admin/bookings
     * Optional filters: hotelId, status
     */
    @GetMapping
    public ResponseEntity<List<Booking>> getAllBookings(
            @RequestParam(required = false) String hotelId,
            @RequestParam(required = false) String status
    ) {
        List<Booking> bookings;

        if (hotelId != null && status != null) {
            bookings = bookingRepo.findByHotelIdAndStatusOrderByCreatedAtDesc(hotelId, status);
        } else if (hotelId != null) {
            bookings = bookingRepo.findByHotelIdOrderByCreatedAtDesc(hotelId);
        } else if (status != null) {
            bookings = bookingRepo.findByStatusOrderByCreatedAtDesc(status);
        } else {
            bookings = bookingRepo.findAllByOrderByCreatedAtDesc();
        }

        return ResponseEntity.ok(bookings);
    }

    /**
     * GET /api/admin/bookings/{id}
     */
    @GetMapping("/{id}")
    public ResponseEntity<Booking> getBookingById(@PathVariable String id) {
        return bookingRepo.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}

package com.example.hotelbackend.controller;

import com.example.hotelbackend.model.HotelDetails;
import com.example.hotelbackend.service.HotelDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotel-details")
public class HotelDetailsController {

    private final HotelDetailsService service;

    public HotelDetailsController(HotelDetailsService service) {
        this.service = service;
    }

    // ⭐ GET HOTEL DETAILS (Frontend)
    @GetMapping("/{hotelId}")
    public ResponseEntity<HotelDetails> getByHotelId(
            @PathVariable String hotelId) {
        return ResponseEntity.ok(service.getByHotelId(hotelId));
    }

    // ⭐ GET ALL HOTELS (Admin)
    @GetMapping
    public ResponseEntity<List<HotelDetails>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // ⭐ CREATE HOTEL DETAILS (Admin)
    @PostMapping
    public ResponseEntity<HotelDetails> create(
            @RequestBody HotelDetails hotelDetails) {
        return ResponseEntity.ok(service.create(hotelDetails));
    }

    // ⭐ UPDATE HOTEL DETAILS (Admin)
    @PutMapping("/{hotelId}")
    public ResponseEntity<HotelDetails> update(
            @PathVariable String hotelId,
            @RequestBody HotelDetails hotelDetails) {
        return ResponseEntity.ok(service.update(hotelId, hotelDetails));
    }

    // ⭐ DELETE HOTEL DETAILS (Admin)
    @DeleteMapping("/{hotelId}")
    public ResponseEntity<String> delete(
            @PathVariable String hotelId) {
        service.delete(hotelId);
        return ResponseEntity.ok("Hotel details deleted");
    }
}


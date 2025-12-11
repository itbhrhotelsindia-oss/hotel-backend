package com.example.hotelbackend.controller;

import com.example.hotelbackend.model.Hotel;
import com.example.hotelbackend.service.HotelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {

    private final HotelService service;

    public HotelController(HotelService service) {
        this.service = service;
    }

    // ADD ONE HOTEL
    @PostMapping("/")
    public ResponseEntity<Hotel> addHotel(@RequestBody Hotel hotel) {
        return ResponseEntity.ok(service.addHotel(hotel));
    }

    // ADD MULTIPLE HOTELS
    @PostMapping("/bulk")
    public ResponseEntity<List<Hotel>> addHotels(@RequestBody List<Hotel> hotels) {
        return ResponseEntity.ok(service.addHotels(hotels));
    }

    // List all cities
    @GetMapping("/cities")
    public ResponseEntity<List<String>> getCities() {
        return ResponseEntity.ok(service.getAllCities());
    }

    // Get hotels in one city
    @GetMapping("/{city}")
    public ResponseEntity<List<Hotel>> getHotelsByCity(@PathVariable String city) {
        return ResponseEntity.ok(service.getHotelsByCity(city));
    }
}


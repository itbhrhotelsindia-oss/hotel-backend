package com.example.hotelbackend.controller;

import com.example.hotelbackend.model.CityHotels;
import com.example.hotelbackend.service.CityHotelsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cities")
public class CityHotelsController {

    private final CityHotelsService service;

    public CityHotelsController(CityHotelsService service) {
        this.service = service;
    }

    // ⭐ GET ALL CITIES WITH HOTELS
    @GetMapping("/")
    public ResponseEntity<List<CityHotels>> getAll() {
        return ResponseEntity.ok(service.getAll());
    }

    // ⭐ GET ONE CITY BY ID
    @GetMapping("/{id}")
    public ResponseEntity<CityHotels> getOne(@PathVariable String id) {
        return ResponseEntity.ok(service.getById(id));
    }

    // ⭐ CREATE CITY + HOTELS
    @PostMapping("/")
    public ResponseEntity<CityHotels> create(@RequestBody CityHotels cityHotels) {
        return ResponseEntity.ok(service.create(cityHotels));
    }

    // ⭐ BULK INSERT — Add Multiple Cities at Once
    @PostMapping("/bulk")
    public ResponseEntity<List<CityHotels>> createBulk(@RequestBody List<CityHotels> list) {
        return ResponseEntity.ok(service.createBulk(list));
    }


    // ⭐ UPDATE CITY + HOTELS
    @PutMapping("/{id}")
    public ResponseEntity<CityHotels> update(@PathVariable String id, @RequestBody CityHotels cityHotels) {
        return ResponseEntity.ok(service.update(id, cityHotels));
    }

    // ⭐ DELETE CITY WITH ALL HOTELS
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable String id) {
        service.delete(id);
        return ResponseEntity.ok("City deleted");
    }
}

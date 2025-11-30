package com.example.hotelbackend.controller;

import com.example.hotelbackend.model.Hotel;
import com.example.hotelbackend.service.HotelService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/hotels")
public class HotelController {
    private final HotelService hotelService;
    public HotelController(HotelService hotelService) {
        this.hotelService = hotelService;
    }

    @GetMapping
    public ResponseEntity<List<Hotel>> list(@RequestParam(name = "city", required = false) String city) {
        if (city != null && !city.trim().isEmpty()) {
            return ResponseEntity.ok(hotelService.findByCity(city));
        } else {
            return ResponseEntity.ok(hotelService.getAll());
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Hotel> get(@PathVariable(name = "id") String id) {
        return hotelService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Hotel> create(@RequestBody Hotel hotel) {
        Hotel saved = hotelService.create(hotel);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Hotel> update(@PathVariable String id, @RequestBody Hotel hotel) {
        hotel.setId(id);
        return ResponseEntity.ok(hotelService.update(hotel));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable String id) {
        hotelService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

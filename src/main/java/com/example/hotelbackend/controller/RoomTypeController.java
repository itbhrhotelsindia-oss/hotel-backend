package com.example.hotelbackend.controller;

import com.example.hotelbackend.model.RoomType;
import com.example.hotelbackend.service.RoomTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/room-types")
public class RoomTypeController {

    private final RoomTypeService service;

    public RoomTypeController(RoomTypeService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<RoomType>> getActiveRoomTypes(@RequestParam String hotelId) {
        return ResponseEntity.ok(service.getActiveRoomTypes(hotelId));
    }
}

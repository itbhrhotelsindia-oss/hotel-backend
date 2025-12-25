package com.example.hotelbackend.controller;

import com.example.hotelbackend.dto.roomtype.BulkCreateRoomTypeRequest;
import com.example.hotelbackend.dto.roomtype.CreateRoomTypeRequest;
import com.example.hotelbackend.dto.roomtype.UpdateRoomTypeRequest;
import com.example.hotelbackend.model.RoomType;
import com.example.hotelbackend.service.RoomTypeService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/room-types")
public class RoomTypeAdminController {

    private final RoomTypeService service;

    public RoomTypeAdminController(RoomTypeService service) {
        this.service = service;
    }

    @PostMapping
    public RoomType create(@RequestBody CreateRoomTypeRequest request) {
        return service.createRoomType(request);
    }

    @GetMapping
    public List<RoomType> list(@RequestParam String hotelId) {
        return service.getActiveRoomTypes(hotelId);
    }

    @PutMapping("/{id}")
    public RoomType update(
            @PathVariable String id,
            @RequestBody UpdateRoomTypeRequest request) {
        return service.updateRoomType(id, request);
    }

    @DeleteMapping("/{id}")
    public void disable(@PathVariable String id) {
        service.disableRoomType(id);
    }

    @PostMapping("/bulk")
    public List<RoomType> bulkCreate(
            @RequestBody BulkCreateRoomTypeRequest request) {
        return service.bulkCreateRoomTypes(request);
    }

}

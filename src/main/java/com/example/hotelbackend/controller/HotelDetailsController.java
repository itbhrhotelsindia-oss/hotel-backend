package com.example.hotelbackend.controller;

import com.example.hotelbackend.dto.roomtype.ImageUploadResponse;
import com.example.hotelbackend.model.HotelDetails;
import com.example.hotelbackend.service.HotelDetailsService;
import com.example.hotelbackend.service.ImageKitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/hotel-details")
public class HotelDetailsController {

    private final HotelDetailsService service;
    private final ImageKitService imageKitService;

    public HotelDetailsController(HotelDetailsService service, ImageKitService imageKitService) {
        this.service = service;
        this.imageKitService = imageKitService;
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

    // ⭐ TOGGLE HOTEL DETAILS STATUS (ACTIVE / INACTIVE)
    @PatchMapping("/{hotelId}/status")
    public ResponseEntity<HotelDetails> toggleStatus(
            @PathVariable String hotelId,
            @RequestParam String status) {
        HotelDetails hotel = service.getByHotelId(hotelId);
        hotel.setStatus(status);
        return ResponseEntity.ok(service.update(hotelId, hotel));
    }

    // 🖼️ UPLOAD HOTEL IMAGE
    @PostMapping("/upload-image")
    public ResponseEntity<ImageUploadResponse> uploadHotelImage(
            @RequestParam("file") MultipartFile file) {
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String folderPath = "Hotels";

            Map<String, Object> result = imageKitService.upload(file, folderPath, fileName);

            String imageUrl = (String) result.get("url");
            String fileId = (String) result.get("fileId");

            return ResponseEntity.ok(new ImageUploadResponse(
                    imageUrl,
                    fileName,
                    fileId
            ));
        } catch (Exception e) {
            return ResponseEntity.status(500).body(new ImageUploadResponse(
                    null,
                    null,
                    "Upload failed: " + e.getMessage()
            ));
        }
    }
}


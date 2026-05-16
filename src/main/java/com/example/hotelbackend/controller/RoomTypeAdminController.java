package com.example.hotelbackend.controller;

import com.example.hotelbackend.dto.roomtype.BulkCreateRoomTypeRequest;
import com.example.hotelbackend.dto.roomtype.CreateRoomTypeRequest;
import com.example.hotelbackend.dto.roomtype.ImageUploadResponse;
import com.example.hotelbackend.dto.roomtype.UpdateRoomTypeRequest;
import com.example.hotelbackend.model.RoomType;
import com.example.hotelbackend.service.ImageKitService;
import com.example.hotelbackend.service.RoomTypeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/room-types")
public class RoomTypeAdminController {

    private final RoomTypeService service;
    private final ImageKitService imageKitService;

    public RoomTypeAdminController(RoomTypeService service, ImageKitService imageKitService) {
        this.service = service;
        this.imageKitService = imageKitService;
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

    // 🖼️ UPLOAD ROOM TYPE IMAGE
    @PostMapping("/{hotelId}/upload-image")
    public ResponseEntity<ImageUploadResponse> uploadRoomTypeImage(
            @PathVariable String hotelId,
            @RequestParam("file") MultipartFile file) {
        try {
            String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            String folderPath = "RoomTypes/" + hotelId;

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

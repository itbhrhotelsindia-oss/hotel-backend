package com.example.hotelbackend.controller;

import com.example.hotelbackend.dto.inventory.UpsertInventoryRequest;
import com.example.hotelbackend.dto.inventory.UpdateInventoryForDateRequest;
import com.example.hotelbackend.dto.inventory.UpdateInventoryStatusRequest;
import com.example.hotelbackend.model.RoomInventory;
import com.example.hotelbackend.service.InventoryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/inventory")
public class InventoryAdminController {

    private final InventoryService service;

    public InventoryAdminController(InventoryService service) {
        this.service = service;
    }

    /* =========================================================
       1️⃣ CREATE / UPDATE INVENTORY (DATE RANGE)
       ========================================================= */

    @PostMapping("/upsert")
    public ResponseEntity<List<RoomInventory>> upsertInventory(
            @RequestBody UpsertInventoryRequest request) {

        return ResponseEntity.ok(service.upsertInventory(request));
    }

    /* =========================================================
       2️⃣ UPDATE INVENTORY FOR SINGLE DATE
       ========================================================= */

    @PutMapping("/date")
    public ResponseEntity<RoomInventory> updateInventoryForDate(
            @RequestBody UpdateInventoryForDateRequest request) {

        return ResponseEntity.ok(service.updateInventoryForDate(request));
    }

    /* =========================================================
       3️⃣ BLOCK / UNBLOCK INVENTORY
       ========================================================= */

    @PutMapping("/status")
    public ResponseEntity<RoomInventory> updateInventoryStatus(
            @RequestBody UpdateInventoryStatusRequest request) {

        return ResponseEntity.ok(service.updateInventoryStatus(request));
    }

    /* =========================================================
       4️⃣ GET INVENTORY (CALENDAR VIEW)
       ========================================================= */

    @GetMapping
    public ResponseEntity<List<RoomInventory>> getInventory(
            @RequestParam String hotelId,
            @RequestParam String roomTypeId,
            @RequestParam String startDate,
            @RequestParam String endDate
    ) {

        return ResponseEntity.ok(
                service.getInventory(hotelId, roomTypeId, startDate, endDate)
        );
    }
}


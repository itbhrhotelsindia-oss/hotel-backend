package com.example.hotelbackend.service;

import com.example.hotelbackend.dto.inventory.UpsertInventoryRequest;
import com.example.hotelbackend.dto.inventory.UpdateInventoryForDateRequest;
import com.example.hotelbackend.dto.inventory.UpdateInventoryStatusRequest;
import com.example.hotelbackend.model.RoomInventory;

import java.util.List;

public interface InventoryService {

    // Owner: create or update inventory for date range
    List<RoomInventory> upsertInventory(UpsertInventoryRequest request);

    // Owner: update price / rooms for single date
    RoomInventory updateInventoryForDate(UpdateInventoryForDateRequest request);

    // Owner: block / unblock a date
    RoomInventory updateInventoryStatus(UpdateInventoryStatusRequest request);

    // Read inventory (used by UI & booking later)
    List<RoomInventory> getInventory(
            String hotelId,
            String roomTypeId,
            String startDate,
            String endDate
    );
}


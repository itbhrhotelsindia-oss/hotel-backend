package com.example.hotelbackend.service;

import com.example.hotelbackend.model.Booking;

public interface InventoryRollbackService {
    void rollbackInventory(Booking booking);
}


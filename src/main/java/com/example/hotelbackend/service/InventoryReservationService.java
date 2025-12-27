package com.example.hotelbackend.service;

import java.time.LocalDate;

public interface InventoryReservationService {

    void reserveInventory(
            String hotelId,
            String roomTypeId,
            LocalDate checkIn,
            LocalDate checkOut,
            int roomsRequested
    );
}

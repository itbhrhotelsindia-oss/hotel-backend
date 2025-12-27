package com.example.hotelbackend.service.impl;

import com.example.hotelbackend.dto.booking.*;
import com.example.hotelbackend.model.RoomInventory;
import com.example.hotelbackend.repository.RoomInventoryRepository;
import com.example.hotelbackend.service.BookingAvailabilityService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingAvailabilityServiceImpl implements BookingAvailabilityService {

    private final RoomInventoryRepository inventoryRepository;

    public BookingAvailabilityServiceImpl(RoomInventoryRepository inventoryRepository) {
        this.inventoryRepository = inventoryRepository;
    }

    @Override
    public Object checkAvailability(CheckAvailabilityRequest request) {

        LocalDate checkIn = LocalDate.parse(request.getCheckIn());
        LocalDate checkOut = LocalDate.parse(request.getCheckOut());

        // 🔴 checkOut is exclusive → subtract 1 day
        LocalDate lastNight = checkOut.minusDays(1);

        // 1️⃣ Fetch all VALID inventory in ONE query
        List<RoomInventory> inventories =
                inventoryRepository.findAvailableInventory(
                        request.getHotelId(),
                        request.getRoomTypeId(),
                        checkIn,
                        checkOut,
                        request.getRoomsRequested() - 1
                );


        // 2️⃣ Validate count (critical)
        long expectedNights = checkIn.until(checkOut).getDays();

        if (inventories.size() != expectedNights) {
            return new AvailabilityFailureResponse(
                    false,
                    "NOT_AVAILABLE_FOR_ALL_DATES",
                    null
            );
        }

        // 3️⃣ Calculate price
        List<PriceBreakup> breakup = new ArrayList<>();
        double totalAmount = 0;

        for (RoomInventory inv : inventories) {
            double dailyPrice = inv.getPricePerNight() * request.getRoomsRequested();

            breakup.add(new PriceBreakup(
                    inv.getDate().toString(),
                    dailyPrice
            ));

            totalAmount += dailyPrice;

        }

        return new AvailabilityResponse(
                true,
                request.getHotelId(),
                request.getRoomTypeId(),
                (int) expectedNights,
                request.getRoomsRequested(),
                (long) totalAmount,
                breakup
        );
    }
}


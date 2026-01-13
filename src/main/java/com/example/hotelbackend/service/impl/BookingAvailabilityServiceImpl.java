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

        List<RoomInventory> inventories = new ArrayList<>();

        // 🔁 CHECK EACH DATE INDIVIDUALLY (STRING DATE MATCH)
        for (LocalDate date = checkIn; date.isBefore(checkOut); date = date.plusDays(1)) {

            String dateKey = date.toString(); // yyyy-MM-dd

            RoomInventory inv = inventoryRepository
                    .findByHotelIdAndRoomTypeIdAndDate(
                            request.getHotelId(),
                            request.getRoomTypeId(),
                            dateKey
                    )
                    .orElse(null);

            if (inv == null
                    || !inv.isActive()
                    || !inv.isPublished()
                    || inv.getAvailableRooms() < request.getRoomsRequested()) {

                return new AvailabilityFailureResponse(
                        false,
                        "NOT_AVAILABLE_FOR_ALL_DATES",
                        dateKey
                );
            }

            inventories.add(inv);
        }

        // 💰 PRICE CALCULATION
        List<PriceBreakup> breakup = new ArrayList<>();
        long totalAmount = 0;

        for (RoomInventory inv : inventories) {
            long dailyPrice =
                    (long) inv.getPricePerNight() * request.getRoomsRequested();

            breakup.add(new PriceBreakup(inv.getDate(), dailyPrice));
            totalAmount += dailyPrice;
        }

        return new AvailabilityResponse(
                true,
                request.getHotelId(),
                request.getRoomTypeId(),
                inventories.size(),
                request.getRoomsRequested(),
                totalAmount,
                breakup
        );
    }
}

package com.example.hotelbackend.service.impl;

import com.example.hotelbackend.dto.booking.*;
import com.example.hotelbackend.model.RoomInventory;
import com.example.hotelbackend.model.RoomType;
import com.example.hotelbackend.repository.RoomInventoryRepository;
import com.example.hotelbackend.repository.RoomTypeRepository;
import com.example.hotelbackend.service.BookingAvailabilityService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingAvailabilityServiceImpl implements BookingAvailabilityService {

    private final RoomInventoryRepository inventoryRepository;
    private final RoomTypeRepository roomTypeRepository;

    public BookingAvailabilityServiceImpl(
            RoomInventoryRepository inventoryRepository,
            RoomTypeRepository roomTypeRepository
    ) {
        this.inventoryRepository = inventoryRepository;
        this.roomTypeRepository = roomTypeRepository;
    }

    @Override
    public Object checkAvailability(CheckAvailabilityRequest request) {

        LocalDate checkIn = LocalDate.parse(request.getCheckIn());
        LocalDate checkOut = LocalDate.parse(request.getCheckOut());

        List<RoomInventory> inventories = new ArrayList<>();

        for (LocalDate date = checkIn; date.isBefore(checkOut); date = date.plusDays(1)) {

            String dateKey = date.toString();

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

        // ✅ Fetch RoomType explicitly
        RoomType roomType = roomTypeRepository
                .findById(request.getRoomTypeId())
                .orElseThrow(() -> new RuntimeException("RoomType not found"));

        int nights = inventories.size();
        int rooms = request.getRoomsRequested();

        double basePrice = inventories.get(0).getPricePerNight();

        List<PriceBreakup> options = new ArrayList<>();

        // 1️⃣ ROOM ONLY – PAY NOW
        options.add(buildOption(
                "ROOM_ONLY",
                "PAY_NOW",
                basePrice,
                nights,
                rooms
        ));

        // 2️⃣ ROOM + BREAKFAST
        options.add(buildOption(
                "ROOM_WITH_BREAKFAST",
                "PAY_NOW",
                basePrice + roomType.getBreakfastPrice(),
                nights,
                rooms
        ));

        // 3️⃣ ROOM + MEALS
        options.add(buildOption(
                "ROOM_WITH_MEALS",
                "PAY_NOW",
                basePrice + roomType.getBreakfastPrice() + roomType.getLunchDinnerPrice(),
                nights,
                rooms
        ));

        // 4️⃣ PAY AT HOTEL (markup on base price)
        double payAtHotelPrice =
                basePrice * (1 + roomType.getPayAtHotelMarkupPercent() / 100);

        options.add(buildOption(
                "ROOM_ONLY",
                "PAY_AT_HOTEL",
                payAtHotelPrice,
                nights,
                rooms
        ));

        return new AvailabilityResponse(
                true,
                request.getHotelId(),
                request.getRoomTypeId(),
                nights,
                rooms,
                options
        );
    }

    private PriceBreakup buildOption(
            String type,
            String payMode,
            double pricePerNight,
            int nights,
            int rooms
    ) {
        double total = pricePerNight * nights * rooms;
        return new PriceBreakup(type, payMode, pricePerNight, total);
    }
}


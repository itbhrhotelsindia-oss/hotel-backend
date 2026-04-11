package com.example.hotelbackend.service.impl;

import com.example.hotelbackend.dto.booking.*;
import com.example.hotelbackend.model.RoomInventory;
import com.example.hotelbackend.model.RoomType;
import com.example.hotelbackend.repository.RoomInventoryRepository;
import com.example.hotelbackend.repository.RoomTypeRepository;
import com.example.hotelbackend.service.BookingAvailabilityService;
import org.springframework.stereotype.Service;
import com.example.hotelbackend.repository.AgentInventoryPriceRepository;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import com.example.hotelbackend.model.AgentInventoryPrice;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class BookingAvailabilityServiceImpl implements BookingAvailabilityService {

    private final RoomInventoryRepository inventoryRepository;
    private final RoomTypeRepository roomTypeRepository;
    private final AgentInventoryPriceRepository agentPriceRepository;

    public BookingAvailabilityServiceImpl(
            RoomInventoryRepository inventoryRepository,
            RoomTypeRepository roomTypeRepository, AgentInventoryPriceRepository agentPriceRepository
    ) {
        this.inventoryRepository = inventoryRepository;
        this.roomTypeRepository = roomTypeRepository;
        this.agentPriceRepository = agentPriceRepository;
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

        // ✅ Fetch RoomType
        RoomType roomType = roomTypeRepository
                .findById(request.getRoomTypeId())
                .orElseThrow(() -> new RuntimeException("RoomType not found"));

        int nights = inventories.size();
        int rooms = request.getRoomsRequested();

        String agentId = getAgentIdIfAgent();

        double pricePerNight =
                inventories.get(0).getPricePerNight();


        if (agentId != null) {

            String date =
                    inventories.get(0).getDate();

            AgentInventoryPrice agentPrice =
                    agentPriceRepository
                            .findByAgentIdAndHotelIdAndRoomTypeIdAndDate(
                                    agentId,
                                    request.getHotelId(),
                                    request.getRoomTypeId(),
                                    date
                            )
                            .orElse(null);

            if (agentPrice != null) {

                pricePerNight =
                        agentPrice.getRoomOnlyPrice();

            }

        }

        BigDecimal basePrice =
                BigDecimal.valueOf(pricePerNight);

        List<PriceBreakup> options = new ArrayList<>();

        // 1️⃣ ROOM ONLY – PAY NOW
        options.add(buildOption(
                "ROOM_ONLY",
                "PAY_NOW",
                basePrice,
                nights,
                rooms
        ));

        // 2️⃣ ROOM WITH BREAKFAST – PAY NOW
        options.add(buildOption(
                "ROOM_WITH_BREAKFAST",
                "PAY_NOW",
                basePrice.add(BigDecimal.valueOf(roomType.getBreakfastPrice())),
                nights,
                rooms
        ));

        // 3️⃣ ROOM WITH MEALS – PAY NOW
        options.add(buildOption(
                "ROOM_WITH_MEALS",
                "PAY_NOW",
                basePrice
                        .add(BigDecimal.valueOf(roomType.getBreakfastPrice()))
                        .add(BigDecimal.valueOf(roomType.getLunchDinnerPrice())),
                nights,
                rooms
        ));

        // 4️⃣ ROOM ONLY – PAY AT HOTEL (markup)
        BigDecimal payAtHotelPrice =
                basePrice.multiply(
                        BigDecimal.ONE.add(
                                BigDecimal.valueOf(roomType.getPayAtHotelMarkupPercent())
                                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)
                        )
                );

        options.add(buildOption(
                "ROOM_ONLY",
                "PAY_AT_HOTEL",
                payAtHotelPrice,
                nights,
                rooms
        ));

        options.add(buildOption(
                "ROOM_WITH_BREAKFAST",
                "PAY_AT_HOTEL",
                payAtHotelPrice.add(BigDecimal.valueOf(roomType.getBreakfastPrice())),
                nights,
                rooms
        ));


        options.add(buildOption(
                "ROOM_WITH_MEALS",
                "PAY_AT_HOTEL",
                payAtHotelPrice
                        .add(BigDecimal.valueOf(roomType.getBreakfastPrice()))
                        .add(BigDecimal.valueOf(roomType.getLunchDinnerPrice())),
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
            BigDecimal pricePerNight,
            int nights,
            int rooms
    ) {
        BigDecimal total =
                pricePerNight
                        .multiply(BigDecimal.valueOf(nights))
                        .multiply(BigDecimal.valueOf(rooms));

        return new PriceBreakup(
                type,
                payMode,
                pricePerNight.setScale(0, RoundingMode.HALF_UP).doubleValue(),
                total.setScale(0, RoundingMode.HALF_UP).doubleValue()
        );
    }

    private String getAgentIdIfAgent() {

        Authentication auth =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (auth == null)
            return null;

        Object principal = auth.getPrincipal();

        if (principal instanceof UserDetails user) {

            boolean isAgent =
                    user.getAuthorities()
                            .stream()
                            .anyMatch(a ->
                                    a.getAuthority()
                                            .equals("ROLE_AGENT")
                            );

            if (isAgent) {
                return user.getUsername();
            }

        }

        return null;
    }
}

package com.example.hotelbackend.service.impl;

import com.example.hotelbackend.dto.inventory.UpsertInventoryRequest;
import com.example.hotelbackend.dto.inventory.UpdateInventoryForDateRequest;
import com.example.hotelbackend.dto.inventory.UpdateInventoryStatusRequest;
import com.example.hotelbackend.model.RoomInventory;
import com.example.hotelbackend.repository.RoomInventoryRepository;
import com.example.hotelbackend.service.InventoryService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final RoomInventoryRepository repository;

    public InventoryServiceImpl(RoomInventoryRepository repository) {
        this.repository = repository;
    }

    /* =========================================================
       CREATE / UPDATE INVENTORY FOR DATE RANGE
       ========================================================= */

    @Override
    public List<RoomInventory> upsertInventory(UpsertInventoryRequest request) {

        validateDateRange(request.getStartDate(), request.getEndDate());

        List<RoomInventory> result = new ArrayList<>();

        LocalDate date = request.getStartDate();

        while (!date.isAfter(request.getEndDate())) {

            RoomInventory inventory = repository
                    .findByHotelIdAndRoomTypeIdAndDate(
                            request.getHotelId(),
                            request.getRoomTypeId(),
                            date
                    )
                    .orElse(null);

            if (inventory == null) {
                inventory = createNewInventory(request, date);
            }

            // Update logic
            inventory.setTotalRooms(request.getTotalRooms());

            // IMPORTANT RULE
            if (inventory.getAvailableRooms() > request.getTotalRooms()) {
                inventory.setAvailableRooms(request.getTotalRooms());
            }

            inventory.setPricePerNight(request.getPricePerNight());
            inventory.setActive(true);

            result.add(repository.save(inventory));

            date = date.plusDays(1);
        }

        return result;
    }

    /* =========================================================
       UPDATE INVENTORY FOR SINGLE DATE
       ========================================================= */

    @Override
    public RoomInventory updateInventoryForDate(UpdateInventoryForDateRequest request) {

        RoomInventory inventory = repository
                .findByHotelIdAndRoomTypeIdAndDate(
                        request.getHotelId(),
                        request.getRoomTypeId(),
                        request.getDate()
                )
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        if (request.getTotalRooms() != null) {

            if (inventory.getAvailableRooms() > request.getTotalRooms()) {
                inventory.setAvailableRooms(request.getTotalRooms());
            }

            inventory.setTotalRooms(request.getTotalRooms());
        }

        if (request.getPricePerNight() != null) {
            inventory.setPricePerNight(request.getPricePerNight());
        }

        return repository.save(inventory);
    }

    /* =========================================================
       BLOCK / UNBLOCK INVENTORY
       ========================================================= */

    @Override
    public RoomInventory updateInventoryStatus(UpdateInventoryStatusRequest request) {

        RoomInventory inventory = repository
                .findByHotelIdAndRoomTypeIdAndDate(
                        request.getHotelId(),
                        request.getRoomTypeId(),
                        request.getDate()
                )
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        inventory.setActive(request.isActive());

        return repository.save(inventory);
    }

    /* =========================================================
       READ INVENTORY (UI / BOOKING)
       ========================================================= */

    @Override
    public List<RoomInventory> getInventory(
            String hotelId,
            String roomTypeId,
            String startDate,
            String endDate
    ) {

        return repository.findByHotelIdAndRoomTypeIdAndDateBetween(
                hotelId,
                roomTypeId,
                LocalDate.parse(startDate),
                LocalDate.parse(endDate)
        );
    }

    /* =========================================================
       PRIVATE HELPERS
       ========================================================= */

    private RoomInventory createNewInventory(
            UpsertInventoryRequest request,
            LocalDate date
    ) {
        return RoomInventory.builder()
                .hotelId(request.getHotelId())
                .roomTypeId(request.getRoomTypeId())
                .date(date)
                .totalRooms(request.getTotalRooms())
                .availableRooms(request.getTotalRooms())
                .pricePerNight(request.getPricePerNight())
                .active(true)
                .build();
    }

    private void validateDateRange(LocalDate start, LocalDate end) {
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }
    }
}


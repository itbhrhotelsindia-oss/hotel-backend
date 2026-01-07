package com.example.hotelbackend.service.impl;

import com.example.hotelbackend.dto.inventory.UpsertInventoryRequest;
import com.example.hotelbackend.dto.inventory.UpdateInventoryForDateRequest;
import com.example.hotelbackend.dto.inventory.UpdateInventoryStatusRequest;
import com.example.hotelbackend.model.RoomInventory;
import com.example.hotelbackend.model.RoomType;
import com.example.hotelbackend.repository.RoomInventoryRepository;
import com.example.hotelbackend.repository.RoomTypeRepository;
import com.example.hotelbackend.service.InventoryService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class InventoryServiceImpl implements InventoryService {

    private final RoomInventoryRepository inventoryRepo;
    private final RoomTypeRepository roomTypeRepo;

    public InventoryServiceImpl(
            RoomInventoryRepository inventoryRepo,
            RoomTypeRepository roomTypeRepo
    ) {
        this.inventoryRepo = inventoryRepo;
        this.roomTypeRepo = roomTypeRepo;
    }

    /* ============================
       UPSERT INVENTORY (DATE RANGE)
       ============================ */
    @Override
    public List<RoomInventory> upsertInventory(UpsertInventoryRequest request) {

        RoomType roomType = roomTypeRepo.findById(request.getRoomTypeId())
                .orElseThrow(() -> new RuntimeException("Room type not found"));

        double basePrice = roomType.getBasePrice();

        List<RoomInventory> result = new ArrayList<>();

        LocalDate date = request.getStartDate();

        while (!date.isAfter(request.getEndDate())) {

            RoomInventory inventory = inventoryRepo
                    .findByHotelIdAndRoomTypeIdAndDate(
                            request.getHotelId(),
                            request.getRoomTypeId(),
                            date
                    )
                    .orElse(RoomInventory.builder()
                            .hotelId(request.getHotelId())
                            .roomTypeId(request.getRoomTypeId())
                            .date(date)
                            .active(true)
                            .build()
                    );

            inventory.setTotalRooms(request.getTotalRooms());
            inventory.setAvailableRooms(request.getTotalRooms());

            inventory.setPricePerNight(
                    request.getPricePerNight() > 0
                            ? request.getPricePerNight()
                            : basePrice
            );

            result.add(inventoryRepo.save(inventory));
            date = date.plusDays(1);
        }

        return result;
    }

    /* ============================
       UPDATE SINGLE DATE
       ============================ */
    @Override
    public RoomInventory updateInventoryForDate(UpdateInventoryForDateRequest request) {

        RoomInventory inventory = inventoryRepo
                .findByHotelIdAndRoomTypeIdAndDate(
                        request.getHotelId(),
                        request.getRoomTypeId(),
                        request.getDate()
                )
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        if (request.getTotalRooms() != null) {
            inventory.setTotalRooms(request.getTotalRooms());
            inventory.setAvailableRooms(request.getTotalRooms());
        }

        if (request.getPricePerNight() != null) {
            inventory.setPricePerNight(request.getPricePerNight());
        }

        return inventoryRepo.save(inventory);
    }

    /* ============================
       BLOCK / UNBLOCK
       ============================ */
    @Override
    public RoomInventory updateInventoryStatus(UpdateInventoryStatusRequest request) {

        RoomInventory inventory = inventoryRepo
                .findByHotelIdAndRoomTypeIdAndDate(
                        request.getHotelId(),
                        request.getRoomTypeId(),
                        request.getDate()
                )
                .orElseThrow(() -> new RuntimeException("Inventory not found"));

        inventory.setActive(request.isActive());
        return inventoryRepo.save(inventory);
    }

    /* ============================
       GET INVENTORY (FULL CALENDAR)
       ============================ */
    @Override
    public List<RoomInventory> getInventory(
            String hotelId,
            String roomTypeId,
            String startDate,
            String endDate
    ) {

        RoomType roomType = roomTypeRepo.findById(roomTypeId)
                .orElseThrow(() -> new RuntimeException("Room type not found"));

        double basePrice = roomType.getBasePrice();

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        List<RoomInventory> existing = inventoryRepo
                .findByHotelIdAndRoomTypeIdAndDateBetween(
                        hotelId, roomTypeId, start, end
                );

        Map<LocalDate, RoomInventory> map = existing.stream()
                .collect(Collectors.toMap(RoomInventory::getDate, i -> i));

        List<RoomInventory> result = new ArrayList<>();

        LocalDate date = start;

        while (!date.isAfter(end)) {

            RoomInventory inventory = map.getOrDefault(
                    date,
                    RoomInventory.builder()
                            .hotelId(hotelId)
                            .roomTypeId(roomTypeId)
                            .date(date)
                            .pricePerNight(basePrice)
                            .totalRooms(0)
                            .availableRooms(0)
                            .active(true)
                            .build()
            );

            result.add(inventory);
            date = date.plusDays(1);
        }

        return result;
    }
}



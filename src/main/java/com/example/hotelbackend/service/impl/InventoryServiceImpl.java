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
import java.util.*;
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

    /* =========================================================
       1️⃣ CREATE / UPDATE INVENTORY (DATE RANGE)
       ========================================================= */

    @Override
    public List<RoomInventory> upsertInventory(UpsertInventoryRequest request) {

        List<RoomInventory> saved = new ArrayList<>();

        LocalDate date = request.getStartDate();

        while (!date.isAfter(request.getEndDate())) {

            String dateKey = date.toString(); // 🔑 FIX

            Optional<RoomInventory> existing =
                    inventoryRepo.findByHotelIdAndRoomTypeIdAndDate(
                            request.getHotelId(),
                            request.getRoomTypeId(),
                            dateKey
                    );

            RoomInventory inventory = existing.orElseGet(() ->
                    RoomInventory.builder()
                            .hotelId(request.getHotelId())
                            .roomTypeId(request.getRoomTypeId())
                            .date(dateKey) // 🔑 FIX
                            .build()
            );

            inventory.setTotalRooms(request.getTotalRooms());
            inventory.setAvailableRooms(request.getTotalRooms());
            inventory.setPricePerNight(request.getPricePerNight());
            inventory.setActive(false);
            inventory.setPublished(false);


            saved.add(inventoryRepo.save(inventory));
            date = date.plusDays(1);
        }

        return saved;
    }

    /* =========================================================
       2️⃣ UPDATE INVENTORY FOR SINGLE DATE
       ========================================================= */

    @Override
    public RoomInventory updateInventoryForDate(
            UpdateInventoryForDateRequest request
    ) {

        String dateKey = request.getDate().toString(); // 🔑 FIX

        RoomInventory inventory = inventoryRepo
                .findByHotelIdAndRoomTypeIdAndDate(
                        request.getHotelId(),
                        request.getRoomTypeId(),
                        dateKey
                )
                .orElseThrow(() ->
                        new RuntimeException("Inventory not found for date")
                );

        if (request.getTotalRooms() != null) {
            inventory.setTotalRooms(request.getTotalRooms());
            inventory.setAvailableRooms(request.getTotalRooms());
        }

        if (request.getPricePerNight() != null) {
            inventory.setPricePerNight(request.getPricePerNight());
        }

        return inventoryRepo.save(inventory);
    }

    /* =========================================================
       3️⃣ BLOCK / UNBLOCK INVENTORY
       ========================================================= */

    @Override
    public RoomInventory updateInventoryStatus(
            UpdateInventoryStatusRequest request
    ) {

        String dateKey = request.getDate().toString(); // 🔑 FIX

        RoomInventory inventory = inventoryRepo
                .findByHotelIdAndRoomTypeIdAndDate(
                        request.getHotelId(),
                        request.getRoomTypeId(),
                        dateKey
                )
                .orElseThrow(() ->
                        new RuntimeException("Inventory not found for date")
                );

        inventory.setActive(request.isActive());
        return inventoryRepo.save(inventory);
    }

    /* =========================================================
       4️⃣ GET INVENTORY (CALENDAR VIEW)
       ========================================================= */

    @Override
    public List<RoomInventory> getInventory(
            String hotelId,
            String roomTypeId,
            String startDate,
            String endDate
    ) {

        RoomType roomType = roomTypeRepo.findById(roomTypeId)
                .orElseThrow(() ->
                        new RuntimeException("Room type not found")
                );

        double basePrice = roomType.getBasePrice();

        LocalDate start = LocalDate.parse(startDate);
        LocalDate end = LocalDate.parse(endDate);

        List<RoomInventory> existing = inventoryRepo
                .findInventoryForRange(
                        hotelId,
                        roomTypeId,
                        startDate,
                        endDate
                );

        Map<String, RoomInventory> map = existing.stream()
                .collect(Collectors.toMap(
                        RoomInventory::getDate,
                        inv -> inv
                ));

        List<RoomInventory> result = new ArrayList<>();
        LocalDate date = start;

        while (!date.isAfter(end)) {

            String dateKey = date.toString(); // 🔑 FIX

            RoomInventory inventory = map.getOrDefault(
                    dateKey,
                    RoomInventory.builder()
                            .hotelId(hotelId)
                            .roomTypeId(roomTypeId)
                            .date(dateKey) // 🔑 FIX
                            .pricePerNight(basePrice)
                            .totalRooms(0)
                            .availableRooms(0)
                            .active(false)          // ❗ inactive by default
                            .published(false)       // ❗ DRAFT
                            .build()
            );

            result.add(inventory);
            date = date.plusDays(1);
        }

        return result;
    }


    @Override
    public void publishInventory(UpsertInventoryRequest request) {

        LocalDate date = request.getStartDate();

        while (!date.isAfter(request.getEndDate())) {

            String dateKey = date.toString();

            RoomInventory inventory =
                    inventoryRepo.findByHotelIdAndRoomTypeIdAndDate(
                                    request.getHotelId(),
                                    request.getRoomTypeId(),
                                    dateKey
                            )
                            .orElseGet(() -> RoomInventory.builder()
                                    .hotelId(request.getHotelId())
                                    .roomTypeId(request.getRoomTypeId())
                                    .date(dateKey)
                                    .build()
                            );

            inventory.setTotalRooms(request.getTotalRooms());
            inventory.setAvailableRooms(request.getTotalRooms());
            inventory.setPricePerNight(request.getPricePerNight());

            inventory.setPublished(true);   // ✅ KEY
            inventory.setActive(true);      // ✅ OPEN after publish

            inventoryRepo.save(inventory);

            date = date.plusDays(1);
        }
    }

}

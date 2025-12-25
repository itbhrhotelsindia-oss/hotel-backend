package com.example.hotelbackend.service.impl;

import com.example.hotelbackend.dto.roomtype.BulkCreateRoomTypeRequest;
import com.example.hotelbackend.dto.roomtype.CreateRoomTypeRequest;
import com.example.hotelbackend.dto.roomtype.UpdateRoomTypeRequest;
import com.example.hotelbackend.model.RoomType;
import com.example.hotelbackend.repository.RoomTypeRepository;
import com.example.hotelbackend.service.RoomTypeService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class RoomTypeServiceImpl implements RoomTypeService {

    private final RoomTypeRepository repository;

    public RoomTypeServiceImpl(RoomTypeRepository repository) {
        this.repository = repository;
    }

    @Override
    public RoomType createRoomType(CreateRoomTypeRequest request) {

        if (repository.existsByHotelIdAndNameIgnoreCase(
                request.getHotelId(), request.getName())) {
            throw new RuntimeException("Room type already exists for this hotel");
        }

        RoomType roomType = RoomType.builder()
                .hotelId(request.getHotelId())
                .name(request.getName())
                .description(request.getDescription())
                .maxGuests(request.getMaxGuests())
                .isActive(true)
                .createdAt(Instant.now())
                .build();

        return repository.save(roomType);
    }

    @Override
    public List<RoomType> getActiveRoomTypes(String hotelId) {
        return repository.findByHotelIdAndIsActiveTrue(hotelId);
    }

    @Override
    public RoomType updateRoomType(String roomTypeId, UpdateRoomTypeRequest request) {

        RoomType roomType = repository.findById(roomTypeId)
                .orElseThrow(() -> new RuntimeException("Room type not found"));

        roomType.setName(request.getName());
        roomType.setDescription(request.getDescription());
        roomType.setMaxGuests(request.getMaxGuests());

        return repository.save(roomType);
    }

    @Override
    public void disableRoomType(String roomTypeId) {

        RoomType roomType = repository.findById(roomTypeId)
                .orElseThrow(() -> new RuntimeException("Room type not found"));

        roomType.setActive(false);
        repository.save(roomType);
    }

    @Override
    public List<RoomType> bulkCreateRoomTypes(BulkCreateRoomTypeRequest request) {

        List<RoomType> created = new ArrayList<>();

        for (CreateRoomTypeRequest rt : request.getRoomTypes()) {

            if (repository.existsByHotelIdAndNameIgnoreCase(
                    request.getHotelId(), rt.getName())) {
                continue; // skip duplicates
            }

            RoomType roomType = RoomType.builder()
                    .hotelId(request.getHotelId())
                    .name(rt.getName())
                    .description(rt.getDescription())
                    .maxGuests(rt.getMaxGuests())
                    .isActive(true)
                    .createdAt(Instant.now())
                    .build();

            created.add(repository.save(roomType));
        }

        return created;
    }

}

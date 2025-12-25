package com.example.hotelbackend.service;

import com.example.hotelbackend.dto.roomtype.BulkCreateRoomTypeRequest;
import com.example.hotelbackend.dto.roomtype.CreateRoomTypeRequest;
import com.example.hotelbackend.dto.roomtype.UpdateRoomTypeRequest;
import com.example.hotelbackend.model.RoomType;

import java.util.List;

public interface RoomTypeService {

    RoomType createRoomType(CreateRoomTypeRequest request);

    List<RoomType> getActiveRoomTypes(String hotelId);

    RoomType updateRoomType(String roomTypeId, UpdateRoomTypeRequest request);

    void disableRoomType(String roomTypeId);

    List<RoomType> bulkCreateRoomTypes(BulkCreateRoomTypeRequest request);

}

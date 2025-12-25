package com.example.hotelbackend.dto.roomtype;

import lombok.Data;
import java.util.List;

@Data
public class BulkCreateRoomTypeRequest {

    private String hotelId;
    private List<CreateRoomTypeRequest> roomTypes;
}


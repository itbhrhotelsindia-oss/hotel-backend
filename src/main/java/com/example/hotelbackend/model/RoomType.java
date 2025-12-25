package com.example.hotelbackend.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "room_types")
public class RoomType {

    @Id
    private String id;

    private String hotelId;

    private String name;
    private String description;
    private int maxGuests;

    private boolean isActive;

    private Instant createdAt;
}

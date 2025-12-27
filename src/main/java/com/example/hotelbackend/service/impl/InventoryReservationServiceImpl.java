package com.example.hotelbackend.service.impl;

import com.example.hotelbackend.model.RoomInventory;
import com.example.hotelbackend.service.InventoryReservationService;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class InventoryReservationServiceImpl implements InventoryReservationService {

    private final MongoTemplate mongoTemplate;

    public InventoryReservationServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void reserveInventory(
            String hotelId,
            String roomTypeId,
            LocalDate checkIn,
            LocalDate checkOut,
            int roomsRequested
    ) {

        for (LocalDate date = checkIn; date.isBefore(checkOut); date = date.plusDays(1)) {

            Query query = new Query();
            query.addCriteria(
                    Criteria.where("hotelId").is(hotelId)
                            .and("roomTypeId").is(roomTypeId)
                            .and("date").is(date)
                            .and("active").is(true)
                            .and("availableRooms").gte(roomsRequested)
            );

            Update update = new Update()
                    .inc("availableRooms", -roomsRequested);

            RoomInventory updated =
                    mongoTemplate.findAndModify(
                            query,
                            update,
                            RoomInventory.class
                    );

            // ❌ If even one day fails → stop immediately
            if (updated == null) {
                throw new RuntimeException(
                        "Inventory reservation failed for date: " + date
                );
            }
        }
    }
}


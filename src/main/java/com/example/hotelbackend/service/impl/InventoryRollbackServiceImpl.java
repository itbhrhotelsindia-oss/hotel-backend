package com.example.hotelbackend.service.impl;

import com.example.hotelbackend.model.Booking;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import com.example.hotelbackend.service.InventoryRollbackService;

import java.time.LocalDate;

@Service
public class InventoryRollbackServiceImpl implements InventoryRollbackService {

    private final MongoTemplate mongoTemplate;

    public InventoryRollbackServiceImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public void rollbackInventory(Booking booking) {

        for (LocalDate date = booking.getCheckIn();
             date.isBefore(booking.getCheckOut());
             date = date.plusDays(1)) {

            Query query = new Query();
            query.addCriteria(
                    Criteria.where("hotelId").is(booking.getHotelId())
                            .and("roomTypeId").is(booking.getRoomTypeId())
                            .and("date").is(date)
            );

            Update update = new Update()
                    .inc("availableRooms", booking.getRooms());

            mongoTemplate.updateFirst(query, update, "room_inventory");
        }
    }
}


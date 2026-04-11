package com.example.hotelbackend.repository;

import com.example.hotelbackend.model.AgentInventoryPrice;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface AgentInventoryPriceRepository
        extends MongoRepository<AgentInventoryPrice, String> {

    Optional<AgentInventoryPrice>
    findByAgentIdAndHotelIdAndRoomTypeIdAndDate(
            String agentId,
            String hotelId,
            String roomTypeId,
            String date
    );



    List<AgentInventoryPrice>
    findByAgentIdAndHotelIdAndRoomTypeId(
            String agentId,
            String hotelId,
            String roomTypeId
    );

}
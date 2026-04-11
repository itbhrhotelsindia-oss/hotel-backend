package com.example.hotelbackend.service;

import com.example.hotelbackend.dto.agent.UpsertAgentPricingRequest;
import com.example.hotelbackend.model.AgentInventoryPrice;
import com.example.hotelbackend.repository.AgentInventoryPriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AgentPricingService {

    private final AgentInventoryPriceRepository repo;


    public void upsertAgentPricing(
            UpsertAgentPricingRequest request
    ) {

        LocalDate start =
                LocalDate.parse(request.getStartDate());

        LocalDate end =
                LocalDate.parse(request.getEndDate());



        while (!start.isAfter(end)) {

            String date = start.toString();

            AgentInventoryPrice price =
                    repo.findByAgentIdAndHotelIdAndRoomTypeIdAndDate(
                            request.getAgentId(),
                            request.getHotelId(),
                            request.getRoomTypeId(),
                            date
                    ).orElse(
                            AgentInventoryPrice.builder()
                                    .agentId(request.getAgentId())
                                    .hotelId(request.getHotelId())
                                    .roomTypeId(request.getRoomTypeId())
                                    .date(date)
                                    .build()
                    );



            price.setRoomOnlyPrice(
                    request.getRoomOnlyPrice());

            price.setBreakfastPrice(
                    request.getBreakfastPrice());

            price.setMealPrice(
                    request.getMealPrice());

            price.setActive(true);



            repo.save(price);

            start = start.plusDays(1);
        }

    }

}
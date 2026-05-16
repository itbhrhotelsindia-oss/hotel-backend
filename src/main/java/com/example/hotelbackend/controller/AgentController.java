package com.example.hotelbackend.controller;

import com.example.hotelbackend.config.JwtUtil;
import com.example.hotelbackend.dto.owner.OwnerHotelResponse;
import com.example.hotelbackend.model.Agent;
import com.example.hotelbackend.model.CityHotels;
import com.example.hotelbackend.model.Hotel;
import com.example.hotelbackend.repository.AgentRepository;
import com.example.hotelbackend.repository.CityHotelsRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/agent")
@RequiredArgsConstructor
public class AgentController {

    private final AgentRepository agentRepo;
    private final CityHotelsRepository cityRepo;
    private final JwtUtil jwtUtil;


    /* =====================================================
       GET AGENT HOTELS
       ===================================================== */

    @GetMapping("/hotels")
    public List<OwnerHotelResponse> getAgentHotels(
            @RequestHeader("Authorization") String authHeader
    ) {

        String token =
                authHeader.replace("Bearer ", "");

        String username =
                jwtUtil.extractUsername(token);

        Agent agent =
                agentRepo.findByUsername(username)
                        .orElseThrow(() ->
                                new RuntimeException("Agent not found")
                        );

        List<OwnerHotelResponse> response =
                new ArrayList<>();

        List<CityHotels> cities =
                cityRepo.findAll();

        for (CityHotels city : cities) {

            for (Hotel hotel : city.getHotels()) {

                if (agent.getHotelIds()
                        .contains(hotel.getHotelId())) {

                    response.add(
                            new OwnerHotelResponse(
                                    hotel.getHotelId(),
                                    hotel.getName(),
                                    city.getName()
                            )
                    );

                }

            }

        }

        return response;

    }

}
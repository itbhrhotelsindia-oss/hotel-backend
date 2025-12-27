package com.example.hotelbackend.controller;

import com.example.hotelbackend.config.JwtUtil;
import com.example.hotelbackend.dto.owner.OwnerHotelResponse;
import com.example.hotelbackend.model.CityHotels;
import com.example.hotelbackend.model.Hotel;
import com.example.hotelbackend.model.Owner;
import com.example.hotelbackend.repository.CityHotelsRepository;
import com.example.hotelbackend.repository.OwnerRepository;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/owner")
public class OwnerController {

    private final JwtUtil jwtUtil;
    private final OwnerRepository ownerRepo;
    private final CityHotelsRepository cityRepo;

    public OwnerController(
            JwtUtil jwtUtil,
            OwnerRepository ownerRepo,
            CityHotelsRepository cityRepo
    ) {
        this.jwtUtil = jwtUtil;
        this.ownerRepo = ownerRepo;
        this.cityRepo = cityRepo;
    }

    @GetMapping("/hotels")
    public List<OwnerHotelResponse> getOwnerHotels(HttpServletRequest request) {

        String token = request.getHeader("Authorization").substring(7);
        String ownerId = jwtUtil.getOwnerId(token);

        Owner owner = ownerRepo.findById(ownerId)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        List<OwnerHotelResponse> response = new ArrayList<>();

        List<CityHotels> cities = cityRepo.findAll();

        for (CityHotels city : cities) {
            for (Hotel hotel : city.getHotels()) {
                if (owner.getHotelIds().contains(hotel.getHotelId())) {
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


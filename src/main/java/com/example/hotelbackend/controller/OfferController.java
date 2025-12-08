package com.example.hotelbackend.controller;

import com.example.hotelbackend.dto.OfferDTO;
import com.example.hotelbackend.service.OfferService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offers")
@RequiredArgsConstructor
public class OfferController {

    private final OfferService offerService;

    @GetMapping
    public List<OfferDTO> getAllOffers() {
        return offerService.getOffers();
    }

    @PutMapping
    public List<OfferDTO> updateOffers(@RequestBody List<OfferDTO> offerList) {
        return offerService.updateOffers(offerList);
    }
}


package com.example.hotelbackend.service;

import com.example.hotelbackend.dto.OfferDTO;
import java.util.List;

public interface OfferService {

    /**
     * Fetch all offers shown on the Offers page
     */
    List<OfferDTO> getOffers();

    /**
     * Replace/update the offers list (full update)
     */
    List<OfferDTO> updateOffers(List<OfferDTO> dtoList);
}


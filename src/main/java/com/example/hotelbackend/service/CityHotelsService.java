package com.example.hotelbackend.service;

import com.example.hotelbackend.dto.HotelLookupResponse;
import com.example.hotelbackend.model.CityHotels;
import com.example.hotelbackend.model.Hotel;
import com.example.hotelbackend.repository.CityHotelsRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CityHotelsService {

    private final CityHotelsRepository repo;

    public CityHotelsService(CityHotelsRepository repo) {
        this.repo = repo;
    }

    /* =========================
       READ OPERATIONS
       ========================= */

    public List<CityHotels> getAll() {
        return repo.findAll();
    }

    public CityHotels getById(String id) {
        return repo.findById(id).orElse(null);
    }

    /* =========================
       CREATE
       ========================= */

    public CityHotels create(CityHotels cityHotels) {

        assignHotelIdsIfMissing(cityHotels);

        return repo.save(cityHotels);
    }

    /* =========================
       UPDATE
       ========================= */

    public CityHotels update(String id, CityHotels updated) {

        updated.setId(id);

        assignHotelIdsIfMissing(updated);

        return repo.save(updated);
    }

    /* =========================
       BULK CREATE
       ========================= */

    public List<CityHotels> createBulk(List<CityHotels> list) {

        for (CityHotels cityHotels : list) {
            assignHotelIdsIfMissing(cityHotels);
        }

        return repo.saveAll(list);
    }

    /* =========================
       DELETE
       ========================= */

    public void delete(String id) {
        repo.deleteById(id);
    }

    /* =========================
       PRIVATE HELPERS
       ========================= */

    /**
     * Assigns hotelId to hotels ONLY if missing.
     * Existing hotelId is NEVER changed.
     */
    private void assignHotelIdsIfMissing(CityHotels cityHotels) {

        if (cityHotels.getHotels() == null || cityHotels.getHotels().isEmpty()) {
            return;
        }

        String cityCode = cityHotels.getName()
                .toUpperCase()
                .replace(" ", "");

        int index = 1;

        for (Hotel hotel : cityHotels.getHotels()) {

            if (hotel.getHotelId() == null || hotel.getHotelId().isEmpty()) {

                hotel.setHotelId(
                        generateHotelId(cityCode, index)
                );

                index++;
            }
        }
    }

    /**
     * HOTEL-JIMCORBETT-001
     */
    private String generateHotelId(String cityCode, int index) {
        return "HOTEL-" + cityCode + "-" + String.format("%03d", index);
    }


    public List<HotelLookupResponse> getHotelLookup() {

        List<HotelLookupResponse> result = new ArrayList<>();

        List<CityHotels> cities = repo.findAll();

        for (CityHotels city : cities) {

            if (city.getHotels() == null) continue;

            for (var hotel : city.getHotels()) {

                result.add(new HotelLookupResponse(
                        city.getId(),
                        city.getName(),
                        hotel.getHotelId(),
                        hotel.getName()
                ));
            }
        }

        return result;
    }

    public CityHotels addHotels(String cityId, List<Hotel> newHotels) {

        CityHotels city = repo.findById(cityId)
                .orElseThrow(() -> new RuntimeException("City not found"));

        if (city.getHotels() == null) {
            city.setHotels(new ArrayList<>());
        }

        int index = city.getHotels().size() + 1;

        for (Hotel hotel : newHotels) {

            if (hotel.getHotelId() == null || hotel.getHotelId().isEmpty()) {
                hotel.setHotelId(
                        generateHotelId(
                                city.getName().toUpperCase().replace(" ", ""),
                                index
                        )
                );
                index++;
            }

            city.getHotels().add(hotel);
        }

        return repo.save(city);
    }


}

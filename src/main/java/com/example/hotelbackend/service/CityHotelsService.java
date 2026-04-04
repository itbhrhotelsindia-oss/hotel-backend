package com.example.hotelbackend.service;

import com.example.hotelbackend.dto.HotelLookupResponse;
import com.example.hotelbackend.model.CityHotels;
import com.example.hotelbackend.model.Hotel;
import com.example.hotelbackend.repository.CityHotelsRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CityHotelsService {

    private final CityHotelsRepository repo;

    public CityHotelsService(CityHotelsRepository repo) {
        this.repo = repo;
    }

    /* =========================
       READ OPERATIONS
       ========================= */


    public List<CityHotels> getAllWithInactive() {

        // ⭐ No filtering — return raw data
        return repo.findAll();
    }

    public List<CityHotels> getAll() {

        List<CityHotels> cities = repo.findAll();

        return cities.stream()

                // ⭐ Filter inactive cities
                .filter(city ->
                        city.getActive() == null ||
                                city.getActive()
                )

                .peek(city -> {

                    if (city.getHotels() == null) return;

                    // ⭐ Filter inactive hotels
                    city.setHotels(
                            city.getHotels()
                                    .stream()
                                    .filter(hotel ->
                                            hotel.getActive() == null ||
                                                    hotel.getActive()
                                    )
                                    .collect(Collectors.toList())
                    );

                })

                .collect(Collectors.toList());
    }
    public CityHotels getById(String id) {

        CityHotels city = repo.findById(id).orElse(null);

        if (city == null) {
            return null;
        }

        // ⭐ Hide inactive city
        if (city.getActive() != null && !city.getActive()) {
            return null;
        }

        if (city.getHotels() != null) {

            city.setHotels(
                    city.getHotels()
                            .stream()
                            .filter(hotel ->
                                    hotel.getActive() == null ||
                                            hotel.getActive()
                            )
                            .collect(Collectors.toList())
            );
        }

        return city;
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

            // ⭐ Set default active = true
            if (hotel.getActive() == null) {
                hotel.setActive(true);
            }

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

                if (hotel.getActive() != null && !hotel.getActive()) {
                    continue;
                }

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

            // ⭐ Ensure active default = true
            if (hotel.getActive() == null) {
                hotel.setActive(true);
            }

            // ⭐ Generate hotel ID if missing
            if (hotel.getHotelId() == null || hotel.getHotelId().isEmpty()) {

                hotel.setHotelId(
                        generateHotelId(
                                city.getName().toUpperCase().replace(" ", ""),
                                index
                        )
                );

                index++;
            }

            // ⭐ Add hotel to city
            city.getHotels().add(hotel);
        }

        return repo.save(city);
    }


    public void deleteHotelByCityId(String cityId, String hotelId) {

        CityHotels city = repo.findById(cityId)
                .orElseThrow(() -> new RuntimeException("City not found"));

        if (city.getHotels() == null || city.getHotels().isEmpty()) {
            throw new RuntimeException("No hotels found for this city");
        }

        boolean removed = city.getHotels().removeIf(
                hotel -> hotelId.equals(hotel.getHotelId())
        );

        if (!removed) {
            throw new RuntimeException("Hotel not found in this city");
        }

        repo.save(city);
    }



}

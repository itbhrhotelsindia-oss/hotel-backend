package com.example.hotelbackend.service;

import com.example.hotelbackend.dto.HotelLookupResponse;
import com.example.hotelbackend.model.CityHotels;
import com.example.hotelbackend.model.Hotel;
import com.example.hotelbackend.repository.CityHotelsRepository;
import com.example.hotelbackend.repository.HotelDetailsRepository;
import com.example.hotelbackend.repository.RoomInventoryRepository;
import com.example.hotelbackend.repository.RoomTypeRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CityHotelsService {

    private final CityHotelsRepository repo;
    private final HotelDetailsRepository hotelDetailsRepo;
    private final RoomTypeRepository roomTypeRepo;
    private final RoomInventoryRepository roomInventoryRepo;

    public CityHotelsService(CityHotelsRepository repo,
                             HotelDetailsRepository hotelDetailsRepo,
                             RoomTypeRepository roomTypeRepo,
                             RoomInventoryRepository roomInventoryRepo) {
        this.repo = repo;
        this.hotelDetailsRepo = hotelDetailsRepo;
        this.roomTypeRepo = roomTypeRepo;
        this.roomInventoryRepo = roomInventoryRepo;
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

        String cityCode = toCityCode(cityHotels.getName());
        Set<String> usedIds = collectAllHotelIds();

        for (Hotel hotel : cityHotels.getHotels()) {

            // ⭐ Set default active = true
            if (hotel.getActive() == null) {
                hotel.setActive(true);
            }

            // ⭐ Preserve existing ids (so edits don't break references),
            //    generate a unique internal id only when one is missing.
            if (hotel.getHotelId() == null || hotel.getHotelId().isEmpty()) {
                String id = generateUniqueHotelId(cityCode, usedIds);
                hotel.setHotelId(id);
                usedIds.add(id);
            } else {
                usedIds.add(hotel.getHotelId());
            }
        }
    }

    /**
     * Collects every hotelId currently in use across all cities,
     * so generated ids are guaranteed globally unique.
     */
    private Set<String> collectAllHotelIds() {
        Set<String> ids = new HashSet<>();
        for (CityHotels city : repo.findAll()) {
            if (city.getHotels() == null) continue;
            for (Hotel h : city.getHotels()) {
                if (h.getHotelId() != null && !h.getHotelId().isEmpty()) {
                    ids.add(h.getHotelId());
                }
            }
        }
        return ids;
    }

    private String toCityCode(String cityName) {
        return cityName == null
                ? "HOTEL"
                : cityName.toUpperCase().replaceAll("[^A-Z0-9]", "");
    }

    /**
     * Generates a unique hotelId of the form HOTEL-JIMCORBETT-001,
     * incrementing the suffix until it no longer collides with usedIds.
     */
    private String generateUniqueHotelId(String cityCode, Set<String> usedIds) {
        int index = 1;
        String candidate;
        do {
            candidate = "HOTEL-" + cityCode + "-" + String.format("%03d", index);
            index++;
        } while (usedIds.contains(candidate));
        return candidate;
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

        String cityCode = toCityCode(city.getName());
        Set<String> usedIds = collectAllHotelIds();

        for (Hotel hotel : newHotels) {

            // ⭐ Ensure active default = true
            if (hotel.getActive() == null) {
                hotel.setActive(true);
            }

            // ⭐ hotelId is ALWAYS generated internally — never trusted from the
            //    client — and guaranteed unique across all cities. This prevents
            //    two hotels ever sharing the same id.
            String id = generateUniqueHotelId(cityCode, usedIds);
            hotel.setHotelId(id);
            usedIds.add(id);

            // ⭐ Add hotel to city
            city.getHotels().add(hotel);
        }

        return repo.save(city);
    }

    // ⭐ TOGGLE CITY ACTIVE STATUS
    public CityHotels toggleCityStatus(String cityId, boolean active) {
        CityHotels city = repo.findById(cityId)
                .orElseThrow(() -> new RuntimeException("City not found"));
        city.setActive(active);
        return repo.save(city);
    }


    public CityHotels toggleHotelStatus(String cityId, String hotelId, boolean active) {
        CityHotels city = repo.findById(cityId)
                .orElseThrow(() -> new RuntimeException("City not found"));

        if (city.getHotels() == null) throw new RuntimeException("No hotels in this city");

        city.getHotels().stream()
                .filter(h -> hotelId.equals(h.getHotelId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Hotel not found"))
                .setActive(active);

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

        // ⭐ Cascade delete: when a hotel is removed, the id and every record
        //    keyed by it is removed too — no orphaned details/rooms/inventory.
        hotelDetailsRepo.deleteById(hotelId);
        roomTypeRepo.deleteByHotelId(hotelId);
        roomInventoryRepo.deleteByHotelId(hotelId);
    }



}

package com.example.hotelbackend.controller;

import com.example.hotelbackend.model.CityHotels;
import com.example.hotelbackend.model.Hotel;
import com.example.hotelbackend.model.HotelDetails;
import com.example.hotelbackend.repository.CityHotelsRepository;
import com.example.hotelbackend.repository.HotelDetailsRepository;
import com.example.hotelbackend.service.ImageKitService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/admin/cleanup")
public class AdminCleanupController {

    private final HotelDetailsRepository hotelDetailsRepo;
    private final CityHotelsRepository cityHotelsRepo;
    private final ImageKitService imageKitService;

    public AdminCleanupController(HotelDetailsRepository hotelDetailsRepo,
                                   CityHotelsRepository cityHotelsRepo,
                                   ImageKitService imageKitService) {
        this.hotelDetailsRepo = hotelDetailsRepo;
        this.cityHotelsRepo   = cityHotelsRepo;
        this.imageKitService  = imageKitService;
    }

    /**
     * GET /api/admin/cleanup/inactive-hotels
     * Preview — returns what would be deleted without deleting anything.
     * Finds hotels that are inactive in EITHER city_hotels OR hotel_details.
     */
    @GetMapping("/inactive-hotels")
    public ResponseEntity<Map<String, Object>> previewInactiveHotels() {
        Set<String> inactiveHotelIds = collectInactiveHotelIds();

        List<Map<String, Object>> preview = new ArrayList<>();
        for (String hotelId : inactiveHotelIds) {
            Map<String, Object> entry = new LinkedHashMap<>();
            entry.put("hotelId", hotelId);

            HotelDetails details = hotelDetailsRepo.findById(hotelId).orElse(null);
            entry.put("name", details != null && details.getBasicInfo() != null
                    ? details.getBasicInfo().getName() : hotelId);
            entry.put("hasDetails", details != null);
            entry.put("images", details != null ? collectImages(details) : List.of());
            preview.add(entry);
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("inactiveCount", inactiveHotelIds.size());
        result.put("hotels", preview);
        return ResponseEntity.ok(result);
    }

    /**
     * DELETE /api/admin/cleanup/inactive-hotels
     * Deletes all inactive hotels from city_hotels + hotel_details + ImageKit images.
     */
    @DeleteMapping("/inactive-hotels")
    public ResponseEntity<Map<String, Object>> deleteInactiveHotels() {
        Set<String> inactiveHotelIds = collectInactiveHotelIds();

        List<String> deletedHotels     = new ArrayList<>();
        List<String> deletedImages     = new ArrayList<>();
        List<String> failedImages      = new ArrayList<>();
        List<String> removedFromCities = new ArrayList<>();

        for (String hotelId : inactiveHotelIds) {

            // 1. Delete ImageKit images from hotel_details
            HotelDetails details = hotelDetailsRepo.findById(hotelId).orElse(null);
            if (details != null) {
                for (String imageUrl : collectImages(details)) {
                    try {
                        imageKitService.deleteByUrl(imageUrl);
                        deletedImages.add(imageUrl);
                    } catch (Exception e) {
                        failedImages.add(imageUrl + " (" + e.getMessage() + ")");
                    }
                }
                hotelDetailsRepo.deleteById(hotelId);
            }

            // 2. Remove hotel from city_hotels
            for (CityHotels city : cityHotelsRepo.findAll()) {
                if (city.getHotels() == null) continue;
                boolean removed = city.getHotels().removeIf(
                        h -> hotelId.equals(h.getHotelId())
                );
                if (removed) {
                    cityHotelsRepo.save(city);
                    removedFromCities.add(hotelId + " from " + city.getName());
                }
            }

            deletedHotels.add(hotelId);
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("deletedHotelsCount", deletedHotels.size());
        result.put("deletedHotels", deletedHotels);
        result.put("deletedImagesCount", deletedImages.size());
        result.put("deletedImages", deletedImages);
        result.put("removedFromCities", removedFromCities);
        if (!failedImages.isEmpty()) result.put("failedImages", failedImages);

        return ResponseEntity.ok(result);
    }

    // ── Collect all hotel IDs that are inactive (from BOTH sources) ──────────
    private Set<String> collectInactiveHotelIds() {
        Set<String> ids = new LinkedHashSet<>();

        // Source 1: hotels in city_hotels with active=false
        for (CityHotels city : cityHotelsRepo.findAll()) {
            if (city.getHotels() == null) continue;
            for (Hotel hotel : city.getHotels()) {
                if (Boolean.FALSE.equals(hotel.getActive())) {
                    ids.add(hotel.getHotelId());
                }
            }
        }

        // Source 2: hotel_details with status=INACTIVE
        for (HotelDetails hd : hotelDetailsRepo.findByStatus("INACTIVE")) {
            ids.add(hd.getHotelId());
        }

        return ids;
    }

    // ── Collect all ImageKit image URLs from a HotelDetails ──────────────────
    private List<String> collectImages(HotelDetails hotel) {
        List<String> images = new ArrayList<>();
        if (hotel.getHotelSlider() != null && hotel.getHotelSlider().getImages() != null)
            hotel.getHotelSlider().getImages().stream().filter(this::isImageKit).forEach(images::add);
        if (hotel.getGallerySection() != null && hotel.getGallerySection().getImages() != null)
            hotel.getGallerySection().getImages().stream().filter(this::isImageKit).forEach(images::add);
        if (hotel.getRoomsSection() != null && hotel.getRoomsSection().getRooms() != null)
            hotel.getRoomsSection().getRooms().forEach(room -> {
                if (room.getImages() != null)
                    room.getImages().stream().filter(this::isImageKit).forEach(images::add);
            });
        return images;
    }

    private boolean isImageKit(String url) {
        return url != null && url.contains("ik.imagekit.io");
    }
}

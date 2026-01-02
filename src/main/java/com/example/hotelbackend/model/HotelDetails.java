package com.example.hotelbackend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "hotel_details")
public class HotelDetails {

    @Id
    private String hotelId; // SAME as HOTEL-JIMCORBETT-001

    private BasicInfo basicInfo;
    private HotelSlider hotelSlider;
    private List<String> services;
    private AboutSection aboutSection;
    private RoomsSection roomsSection;
    private AmenitiesSection amenitiesSection;
    private GallerySection gallerySection;
    private PoliciesSection policiesSection;
    private LocationSection locationSection;
    private FaqSection faqSection;

    private String status; // ACTIVE / INACTIVE

    /* =========================
       INNER CLASSES
       ========================= */

    @Data
    public static class BasicInfo {
        private String name;
        private String city;
        private String state;
        private String address;
    }

    @Data
    public static class HotelSlider {
        private String title;
        private String subtitle;
        private List<String> images;
    }

    @Data
    public static class AboutSection {
        private String title;
        private String description;
    }

    @Data
    public static class RoomsSection {
        private String title;
        private List<Room> rooms;
    }

    @Data
    public static class Room {
        private String roomTypeId; // MUST match room_inventory.roomTypeId
        private String name;
        private String description;
        private List<String> images;
        private List<String> amenities;
    }

    @Data
    public static class AmenitiesSection {
        private String title;
        private List<String> amenities;
    }

    @Data
    public static class GallerySection {
        private List<String> images;
    }

    @Data
    public static class PoliciesSection {
        private String checkInTime;
        private String checkOutTime;
        private boolean petsAllowed;
        private String cancellationPolicy;
    }

    @Data
    public static class LocationSection {
        private String mapEmbedUrl;
        private List<String> nearbyAttractions;
    }

    @Data
    public static class FaqSection {
        private List<Faq> faqs;
    }

    @Data
    public static class Faq {
        private String question;
        private String answer;
    }
}


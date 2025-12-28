// src/main/java/com/example/hotelbackend/dto/HomePageData.java
package com.example.hotelbackend.dto;

import lombok.Data;
import java.util.List;
import java.util.Map;

/**
 * Data Transfer Object for home page content.
 * Contains all the data needed to render the home page.
 */
@Data
public class HomePageData {

    private List<String> heroImages;
    private BrandDynamicSection brandSection;
    private EventsSection eventsSection;
    private AboutSection aboutSection;
    private BrandBanner brandBanner;
    private ContactSection contactSection;

    @Data
    public static class BrandSection {
        private String title;
        private List<Brand> brands;
    }

    @Data
    public static class Brand {
        private String name;
        private String description;
        private String imageUrl;
    }

    @Data
    public static class EventsSection {
        private String title;
        private String description;
        private List<Event> events;
    }

    @Data
    public static class Event {
        private String title;
        private String description;
        private String imageUrl;
    }

    @Data
    public static class AboutSection {
        private String title;
        private String description;
        private String buttonText;
        private String buttonLink;
        private List<Stat> stats;
    }

    @Data
    public static class Stat {
        private String value;
        private String label;
    }

    @Data
    public static class BrandBanner {
        private String title;
        private String subtitle;
        private List<ContactInfo> contacts;
    }

    @Data
    public static class ContactInfo {
        private String type;          // "phone", "email", etc.
        private String value;         // e.g. "18002091400"
        private String displayValue;  // e.g. "1800 209 1400"
    }


    @Data
    public static class ContactSection {

        private String companyName;
        private String companySince;

        private String reservationPhone;
        private String hotelPhone;

        private String visitUs;
        private String email;
        private String watsApp;

        private String corporateAddress;
        private String supportHours;

        // socialLinks is now a MAP instead of List
        private Map<String, String> socialLinks;
    }


    @Data
    public static class SocialLink {
        private String name;
        private String url;
    }
    @Data
    public static class BrandDynamicSection {
        private String title;
        private String description;
        private List<BrandBlock> blocks;
    }

    @Data
    public static class BrandBlock {
        private String name;
        private String layout;   // "text-left-image-right" or "image-left-text-right"
        private String text;
        private String imageUrl;
    }
}

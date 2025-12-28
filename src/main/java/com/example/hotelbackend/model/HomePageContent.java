// src/main/java/com/example/hotelbackend/model/HomePageContent.java
package com.example.hotelbackend.model;

import com.example.hotelbackend.dto.HomePageData;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;

@Data
@Document(collection = "home_page_content")
public class HomePageContent {

    @Id
    private String id;

    private List<String> heroImages;
    private BrandDynamicSection brandSection;
    private EventsSection eventsSection;
    private AboutSection aboutSection;
    private BrandBanner brandBanner;
    private ContactSection contactSection;

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
        private String type;
        private String value;
        private String displayValue;
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
        private String layout;
        private String text;
        private String imageUrl;
    }
}

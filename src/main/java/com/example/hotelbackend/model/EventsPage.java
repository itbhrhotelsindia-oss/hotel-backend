package com.example.hotelbackend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "events_page")
public class EventsPage {

    @Id
    private String id;

    private String pageTitle;
    private EventSlider eventSlider;
    private EventsSection eventsSection;

    /* =====================
       INNER CLASSES
       ===================== */

    @Data
    public static class EventSlider {
        private List<String> images;
        private boolean autoPlay;
        private int interval;
    }

    @Data
    public static class EventsSection {
        private String title;
        private String description;
        private List<EventCategory> eventCategories;
    }

    @Data
    public static class EventCategory {
        private String key;       // family / business
        private String title;
        private String image;
        private String heading;
        private String description;
    }
}


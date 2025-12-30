package com.example.hotelbackend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document("weddingPage")
public class WeddingPage {

    @Id
    private String id;

    private String title;

    private BannerLines bannerLines;

    private String videoUrl;

    private List<Stat> stats;

    private String description;

    private Festivities festivities;

    private TypeOfWeddings typeOfWeddings;

    private List<Highlight> highlights;


    // INNER CLASSES

    @Data
    public static class BannerLines {
        private int left;
        private int right;
        private String color;
    }

    @Data
    public static class Stat {
        private String label;
        private String value;
    }

    @Data
    public static class Festivities {
        private String id;
        private String title;
        private String description;
        private List<FestivityItem> festivitiesList;
    }

    @Data
    public static class FestivityItem {
        private String title;
        private String text;
        private String imageUrl;
        private String layout;
    }

    @Data
    public static class TypeOfWeddings {
        private String id;
        private String title;
        private String description;
        private List<WeddingItem> weddingList;
    }

    @Data
    public static class WeddingItem {
        private String title;
        private String text;
        private String imageUrl;
        private String layout;
    }

    @Data
    public static class Highlight {
        private String iconUrl;
        private String title;
    }
}


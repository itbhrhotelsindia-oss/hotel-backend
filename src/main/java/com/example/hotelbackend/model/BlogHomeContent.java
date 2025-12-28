package com.example.hotelbackend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Document(collection = "blog_home")
public class BlogHomeContent {

    @Id
    private String id;

    private String title;
    private String description;

    private List<String> blogImages;

    private BlogListSection blogListSection;
    private PopularPostListSection popularPostListSection;

    /* =======================
       INNER CLASSES
       ======================= */

    @Data
    public static class BlogListSection {
        private String id;
        private String title;
        private String description;
        private List<BlogItem> blogsList;
    }

    @Data
    public static class PopularPostListSection {
        private String id;
        private String title;
        private String description;
        private List<BlogItem> postsList;
    }

    @Data
    public static class BlogItem {
        private String id;          // slug / unique id
        private String title;
        private String description;
        private String imageUrl;
        private String htmlContent; // rich HTML
    }
}

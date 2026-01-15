package com.example.hotelbackend.dto.legal;

import com.example.hotelbackend.dto.legal.LegalSection;

import java.util.List;

public class LegalPageResponse {

    private String slug;
    private String title;
    private String lastUpdated;
    private List<LegalSection> content;

    public LegalPageResponse(
            String slug,
            String title,
            String lastUpdated,
            List<LegalSection> content
    ) {
        this.slug = slug;
        this.title = title;
        this.lastUpdated = lastUpdated;
        this.content = content;
    }

    public String getSlug() {
        return slug;
    }

    public String getTitle() {
        return title;
    }

    public String getLastUpdated() {
        return lastUpdated;
    }

    public List<LegalSection> getContent() {
        return content;
    }
}


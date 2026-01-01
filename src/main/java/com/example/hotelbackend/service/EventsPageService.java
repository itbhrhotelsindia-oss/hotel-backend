package com.example.hotelbackend.service;

import com.example.hotelbackend.model.EventsPage;

public interface EventsPageService {

    EventsPage getPage();
    EventsPage updatePage(EventsPage page);

    // Slider
    EventsPage addSliderImage(String imageUrl);
    EventsPage deleteSliderImage(int index);

    // Categories
    EventsPage addCategory(EventsPage.EventCategory category);
    EventsPage updateCategory(String key, EventsPage.EventCategory category);
    EventsPage deleteCategory(String key);
}


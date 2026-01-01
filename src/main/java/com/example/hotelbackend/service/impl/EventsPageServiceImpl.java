package com.example.hotelbackend.service.impl;

import com.example.hotelbackend.model.EventsPage;
import com.example.hotelbackend.repository.EventsPageRepository;
import com.example.hotelbackend.service.EventsPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class EventsPageServiceImpl implements EventsPageService {

    private final EventsPageRepository repository;

    private EventsPage getOrCreate() {
        return repository.findFirstBy()
                .orElseGet(() -> {
                    EventsPage page = new EventsPage();
                    page.setId("EVENTS-PAGE-001");
                    return repository.save(page);
                });
    }

    @Override
    public EventsPage getPage() {
        return getOrCreate();
    }

    @Override
    public EventsPage updatePage(EventsPage page) {
        EventsPage existing = getOrCreate();
        page.setId(existing.getId());
        return repository.save(page);
    }

    /* =====================
       SLIDER
       ===================== */

    @Override
    public EventsPage addSliderImage(String imageUrl) {
        EventsPage page = getOrCreate();

        if (page.getEventSlider() == null) {
            page.setEventSlider(new EventsPage.EventSlider());
        }
        if (page.getEventSlider().getImages() == null) {
            page.getEventSlider().setImages(new ArrayList<>());
        }

        page.getEventSlider().getImages().add(imageUrl);
        return repository.save(page);
    }

    @Override
    public EventsPage deleteSliderImage(int index) {
        EventsPage page = getOrCreate();

        if (page.getEventSlider() != null &&
                page.getEventSlider().getImages() != null &&
                index >= 0 &&
                index < page.getEventSlider().getImages().size()) {

            page.getEventSlider().getImages().remove(index);
        }

        return repository.save(page);
    }

    /* =====================
       CATEGORIES
       ===================== */

    @Override
    public EventsPage addCategory(EventsPage.EventCategory category) {
        EventsPage page = getOrCreate();

        if (page.getEventsSection() == null) {
            page.setEventsSection(new EventsPage.EventsSection());
        }
        if (page.getEventsSection().getEventCategories() == null) {
            page.getEventsSection().setEventCategories(new ArrayList<>());
        }

        page.getEventsSection().getEventCategories().add(category);
        return repository.save(page);
    }

    @Override
    public EventsPage updateCategory(String key, EventsPage.EventCategory category) {
        EventsPage page = getOrCreate();

        if (page.getEventsSection() != null &&
                page.getEventsSection().getEventCategories() != null) {

            page.getEventsSection().getEventCategories().removeIf(
                    c -> key.equals(c.getKey())
            );
            page.getEventsSection().getEventCategories().add(category);
        }

        return repository.save(page);
    }

    @Override
    public EventsPage deleteCategory(String key) {
        EventsPage page = getOrCreate();

        if (page.getEventsSection() != null &&
                page.getEventsSection().getEventCategories() != null) {

            page.getEventsSection().getEventCategories()
                    .removeIf(c -> key.equals(c.getKey()));
        }

        return repository.save(page);
    }
}


package com.example.hotelbackend.service.impl;

import com.example.hotelbackend.dto.HomePageData;
import com.example.hotelbackend.model.HomePageContent;
import com.example.hotelbackend.repository.HomePageContentRepository;
import com.example.hotelbackend.service.HomePageService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomePageServiceImpl implements HomePageService {

    private final HomePageContentRepository repository;

    /* ===========================================================
       INITIALIZE DEFAULT DATA ON FIRST RUN
    ============================================================ */
    @PostConstruct
    public void init() {
        if (repository.count() > 0) return;

        HomePageContent content = new HomePageContent();

        // HERO IMAGES
        content.setHeroImages(List.of(
                "/assets/hero1.png",
                "/assets/hero2.png",
                "/assets/brand-2.png",
                "/assets/hero2.png",
                "/assets/brand-2.png"
        ));

        /* ===========================================================
           BRAND SECTION
        ============================================================ */
        HomePageContent.BrandDynamicSection bs = new HomePageContent.BrandDynamicSection();
        bs.setTitle("OUR BRANDS");
        bs.setDescription(
                "At BHR Hotels, we embody the spirit of India through gracious hospitality, refined comfort, and elegantly curated spaces."
        );

        HomePageContent.BrandBlock b1 = new HomePageContent.BrandBlock();
        b1.setName("Pride Plaza");
        b1.setLayout("text-left-image-right");
        b1.setText("Experience the pinnacle of refined Indian luxury at Pride Plaza, our upscale brand created for high-income individuals, C-suite executives, and elite leisure travellers.");
        b1.setImageUrl("https://res.cloudinary.com/dmc1cwiqi/image/upload/v1766733238/home/brand/brand_1.jpg");

        HomePageContent.BrandBlock b2 = new HomePageContent.BrandBlock();
        b2.setName("Pride Premier");
        b2.setLayout("image-left-text-right");
        b2.setText("Dynamic, stylish, and connected to India’s evolving urban lifestyle, Pride Premier is our upper midscale brand designed for affluent business executives and high-income families.");
        b2.setImageUrl("https://res.cloudinary.com/dmc1cwiqi/image/upload/v1766733259/home/brand/brand_2.jpg");

        bs.setBlocks(List.of(b1, b2));
        content.setBrandSection(bs);

        /* ===========================================================
           EVENTS SECTION (FULLY INITIALIZED)
        ============================================================ */
        HomePageContent.EventsSection es = new HomePageContent.EventsSection();
        es.setTitle("PLAN YOUR EVENTS");
        es.setDescription(
                "At BHR Hotels, every event is thoughtfully crafted to reflect your purpose and passion."
        );

        HomePageContent.Event e1 = new HomePageContent.Event();
        e1.setTitle("Sacred Vows & Celebrations");
        e1.setDescription("From traditional ceremonies to lavish receptions, we create timeless wedding experiences with flawless planning and personalized touches.");
        e1.setImageUrl("https://res.cloudinary.com/dmc1cwiqi/image/upload/v1766749191/home/events/events_1.jpg");

        HomePageContent.Event e2 = new HomePageContent.Event();
        e2.setTitle("Moments to Celebrate");
        e2.setDescription("From birthdays to anniversaries, our inviting spaces create the perfect setting for memorable celebrations filled with joy and togetherness.");
        e2.setImageUrl("https://res.cloudinary.com/dmc1cwiqi/image/upload/v1766749227/home/events/events_2.jpg");

        HomePageContent.Event e3 = new HomePageContent.Event();
        e3.setTitle("Business Excellence Events");
        e3.setDescription("Where ideas take shape—host focused business meetings and leadership events in environments built for clarity and collaboration.");
        e3.setImageUrl("https://res.cloudinary.com/dmc1cwiqi/image/upload/v1766749266/home/events/events_3.jpg");

        es.setEvents(List.of(e1, e2, e3));
        content.setEventsSection(es);

        /* ===========================================================
           CONTACT SECTION
        ============================================================ */
        HomePageContent.ContactSection cs = new HomePageContent.ContactSection();
        cs.setCompanyName("BHR Hotels India LLP");
        cs.setCompanySince("2010");
        cs.setReservationPhone("+91 9211283334");
        cs.setVisitUs("www.bhrhotelsindia.com");

        Map<String, String> links = new HashMap<>();
        links.put("facebook", "https://www.facebook.com/bhrhotelsindia");
        links.put("instagram", "https://www.instagram.com/bhrhotelsindiaofficial");
        links.put("youtube", "https://www.youtube.com/@bhrhotelsindia");

        cs.setSocialLinks(links);
        content.setContactSection(cs);

        repository.save(content);
    }

    /* ===========================================================
       GET HOME PAGE DATA
    ============================================================ */
    @Override
    public HomePageData getHomePageData() {
        return repository.findFirstBy()
                .map(this::toDto)
                .orElseGet(HomePageData::new);
    }

    /* ===========================================================
       UPDATE HOME PAGE DATA
    ============================================================ */
    @Override
    public HomePageData updateHomePageData(HomePageData dto) {
        HomePageContent entity = repository.findFirstBy()
                .orElseGet(HomePageContent::new);

        updateEntityFromDto(entity, dto);
        return toDto(repository.save(entity));
    }

    /* ===========================================================
       UPDATE ENTITY FROM DTO
    ============================================================ */
    private void updateEntityFromDto(HomePageContent entity, HomePageData dto) {
        if (dto == null) return;

        if (dto.getHeroImages() != null)
            entity.setHeroImages(dto.getHeroImages());

        // BRAND SECTION
        if (dto.getBrandSection() != null) {
            HomePageContent.BrandDynamicSection existing =
                    entity.getBrandSection() != null
                            ? entity.getBrandSection()
                            : new HomePageContent.BrandDynamicSection();

            if (dto.getBrandSection().getTitle() != null)
                existing.setTitle(dto.getBrandSection().getTitle());

            if (dto.getBrandSection().getDescription() != null)
                existing.setDescription(dto.getBrandSection().getDescription());

            if (dto.getBrandSection().getBlocks() != null) {
                existing.setBlocks(
                        dto.getBrandSection().getBlocks()
                                .stream()
                                .map(this::toBrandBlockEntity)
                                .collect(Collectors.toList())
                );
            }

            entity.setBrandSection(existing);
        }

        // EVENTS SECTION
        if (dto.getEventsSection() != null) {
            HomePageContent.EventsSection existing =
                    entity.getEventsSection() != null
                            ? entity.getEventsSection()
                            : new HomePageContent.EventsSection();

            if (dto.getEventsSection().getTitle() != null)
                existing.setTitle(dto.getEventsSection().getTitle());

            if (dto.getEventsSection().getDescription() != null)
                existing.setDescription(dto.getEventsSection().getDescription());

            if (dto.getEventsSection().getEvents() != null) {
                existing.setEvents(
                        dto.getEventsSection().getEvents()
                                .stream()
                                .map(this::toEventEntity)
                                .collect(Collectors.toList())
                );
            }

            entity.setEventsSection(existing);
        }
    }

    /* ===========================================================
       ENTITY → DTO
    ============================================================ */
    private HomePageData toDto(HomePageContent e) {
        HomePageData dto = new HomePageData();
        dto.setHeroImages(e.getHeroImages());
        dto.setBrandSection(toBrandDynamicDto(e.getBrandSection()));
        dto.setEventsSection(toEventsDto(e.getEventsSection()));
        dto.setContactSection(toContactDto(e.getContactSection()));
        return dto;
    }

    /* ===========================================================
       MAPPERS
    ============================================================ */
    private HomePageData.BrandDynamicSection toBrandDynamicDto(HomePageContent.BrandDynamicSection e) {
        if (e == null) return null;
        HomePageData.BrandDynamicSection dto = new HomePageData.BrandDynamicSection();
        dto.setTitle(e.getTitle());
        dto.setDescription(e.getDescription());

        if (e.getBlocks() != null) {
            dto.setBlocks(
                    e.getBlocks()
                            .stream()
                            .map(this::toBrandBlockDto)
                            .collect(Collectors.toList())
            );
        }
        return dto;
    }

    private HomePageData.BrandBlock toBrandBlockDto(HomePageContent.BrandBlock e) {
        HomePageData.BrandBlock dto = new HomePageData.BrandBlock();
        dto.setName(e.getName());
        dto.setLayout(e.getLayout());
        dto.setText(e.getText());
        dto.setImageUrl(e.getImageUrl());
        return dto;
    }

    private HomePageContent.BrandBlock toBrandBlockEntity(HomePageData.BrandBlock dto) {
        HomePageContent.BrandBlock b = new HomePageContent.BrandBlock();
        b.setName(dto.getName());
        b.setLayout(dto.getLayout());
        b.setText(dto.getText());
        b.setImageUrl(dto.getImageUrl());
        return b;
    }

    private HomePageData.EventsSection toEventsDto(HomePageContent.EventsSection e) {
        if (e == null) return null;

        HomePageData.EventsSection dto = new HomePageData.EventsSection();
        dto.setTitle(e.getTitle());
        dto.setDescription(e.getDescription());

        if (e.getEvents() != null) {
            dto.setEvents(
                    e.getEvents()
                            .stream()
                            .map(this::toEventDto)
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }

    private HomePageData.Event toEventDto(HomePageContent.Event e) {
        HomePageData.Event dto = new HomePageData.Event();
        dto.setTitle(e.getTitle());
        dto.setDescription(e.getDescription());
        dto.setImageUrl(e.getImageUrl());
        return dto;
    }

    private HomePageContent.Event toEventEntity(HomePageData.Event dto) {
        HomePageContent.Event e = new HomePageContent.Event();
        e.setTitle(dto.getTitle());
        e.setDescription(dto.getDescription());
        e.setImageUrl(dto.getImageUrl());
        return e;
    }

    private HomePageData.ContactSection toContactDto(HomePageContent.ContactSection e) {
        if (e == null) return null;
        HomePageData.ContactSection dto = new HomePageData.ContactSection();
        dto.setCompanyName(e.getCompanyName());
        dto.setCompanySince(e.getCompanySince());
        dto.setReservationPhone(e.getReservationPhone());
        dto.setVisitUs(e.getVisitUs());
        dto.setSocialLinks(e.getSocialLinks());
        return dto;
    }
}

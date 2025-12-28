package com.example.hotelbackend.service.impl;

import com.example.hotelbackend.dto.HomePageData;
import com.example.hotelbackend.model.HomePageContent;
import com.example.hotelbackend.repository.HomePageContentRepository;
import com.example.hotelbackend.service.HomePageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HomePageServiceImpl implements HomePageService {

    private final HomePageContentRepository repository;

    /* =========================
       READ
       ========================= */
    @Override
    public HomePageData getHomePageData() {
        return repository.findFirstBy()
                .map(this::toDto)
                .orElseGet(HomePageData::new);
    }

    /* =========================
       UPDATE
       ========================= */
    @Override
    public HomePageData updateHomePageData(HomePageData dto) {
        HomePageContent entity = repository.findFirstBy()
                .orElseGet(HomePageContent::new);

        updateEntityFromDto(entity, dto);
        return toDto(repository.save(entity));
    }

    /* =========================
       ENTITY → DTO
       ========================= */
    private HomePageData toDto(HomePageContent e) {
        HomePageData dto = new HomePageData();

        dto.setHeroImages(e.getHeroImages());
        dto.setBrandSection(toBrandDynamicDto(e.getBrandSection()));
        dto.setEventsSection(toEventsDto(e.getEventsSection()));
        dto.setAboutSection(toAboutDto(e.getAboutSection()));
        dto.setBrandBanner(toBrandBannerDto(e.getBrandBanner()));
        dto.setContactSection(toContactDto(e.getContactSection()));

        return dto;
    }

    /* =========================
       DTO → ENTITY
       ========================= */
    private void updateEntityFromDto(HomePageContent entity, HomePageData dto) {

        if (dto.getHeroImages() != null) {
            entity.setHeroImages(dto.getHeroImages());
        }

        /* BRAND SECTION */
        if (dto.getBrandSection() != null) {
            HomePageContent.BrandDynamicSection bs =
                    entity.getBrandSection() != null
                            ? entity.getBrandSection()
                            : new HomePageContent.BrandDynamicSection();

            bs.setTitle(dto.getBrandSection().getTitle());
            bs.setDescription(dto.getBrandSection().getDescription());

            if (dto.getBrandSection().getBlocks() != null) {
                bs.setBlocks(
                        dto.getBrandSection().getBlocks().stream()
                                .map(this::toBrandBlockEntity)
                                .collect(Collectors.toList())
                );
            }
            entity.setBrandSection(bs);
        }

        /* EVENTS SECTION */
        if (dto.getEventsSection() != null) {
            HomePageContent.EventsSection es =
                    entity.getEventsSection() != null
                            ? entity.getEventsSection()
                            : new HomePageContent.EventsSection();

            es.setTitle(dto.getEventsSection().getTitle());
            es.setDescription(dto.getEventsSection().getDescription());

            if (dto.getEventsSection().getEvents() != null) {
                es.setEvents(
                        dto.getEventsSection().getEvents().stream()
                                .map(this::toEventEntity)
                                .collect(Collectors.toList())
                );
            }
            entity.setEventsSection(es);
        }

        /* ABOUT SECTION */
        if (dto.getAboutSection() != null) {
            HomePageContent.AboutSection as = new HomePageContent.AboutSection();
            as.setTitle(dto.getAboutSection().getTitle());
            as.setDescription(dto.getAboutSection().getDescription());
            as.setButtonText(dto.getAboutSection().getButtonText());
            as.setButtonLink(dto.getAboutSection().getButtonLink());

            if (dto.getAboutSection().getStats() != null) {
                as.setStats(
                        dto.getAboutSection().getStats().stream()
                                .map(s -> {
                                    HomePageContent.Stat st = new HomePageContent.Stat();
                                    st.setValue(s.getValue());
                                    st.setLabel(s.getLabel());
                                    return st;
                                })
                                .collect(Collectors.toList())
                );
            }
            entity.setAboutSection(as);
        }

        /* BRAND BANNER */
        if (dto.getBrandBanner() != null) {
            HomePageContent.BrandBanner bb = new HomePageContent.BrandBanner();
            bb.setTitle(dto.getBrandBanner().getTitle());
            bb.setSubtitle(dto.getBrandBanner().getSubtitle());

            if (dto.getBrandBanner().getContacts() != null) {
                bb.setContacts(
                        dto.getBrandBanner().getContacts().stream()
                                .map(c -> {
                                    HomePageContent.ContactInfo ci =
                                            new HomePageContent.ContactInfo();
                                    ci.setType(c.getType());
                                    ci.setValue(c.getValue());
                                    ci.setDisplayValue(c.getDisplayValue());
                                    return ci;
                                })
                                .collect(Collectors.toList())
                );
            }
            entity.setBrandBanner(bb);
        }

        /* CONTACT SECTION */
        if (dto.getContactSection() != null) {
            HomePageContent.ContactSection cs =
                    entity.getContactSection() != null
                            ? entity.getContactSection()
                            : new HomePageContent.ContactSection();

            cs.setCompanyName(dto.getContactSection().getCompanyName());
            cs.setCompanySince(dto.getContactSection().getCompanySince());
            cs.setReservationPhone(dto.getContactSection().getReservationPhone());
            cs.setHotelPhone(dto.getContactSection().getHotelPhone());
            cs.setVisitUs(dto.getContactSection().getVisitUs());
            cs.setEmail(dto.getContactSection().getEmail());
            cs.setWatsApp(dto.getContactSection().getWatsApp());
            cs.setCorporateAddress(dto.getContactSection().getCorporateAddress());
            cs.setSupportHours(dto.getContactSection().getSupportHours());
            cs.setSocialLinks(dto.getContactSection().getSocialLinks());

            entity.setContactSection(cs);
        }
    }

    /* =========================
       MAPPERS
       ========================= */

    private HomePageData.BrandDynamicSection toBrandDynamicDto(HomePageContent.BrandDynamicSection e) {
        if (e == null) return null;

        HomePageData.BrandDynamicSection dto = new HomePageData.BrandDynamicSection();
        dto.setTitle(e.getTitle());
        dto.setDescription(e.getDescription());

        if (e.getBlocks() != null) {
            dto.setBlocks(
                    e.getBlocks().stream()
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
                    e.getEvents().stream()
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

    private HomePageData.AboutSection toAboutDto(HomePageContent.AboutSection e) {
        if (e == null) return null;

        HomePageData.AboutSection dto = new HomePageData.AboutSection();
        dto.setTitle(e.getTitle());
        dto.setDescription(e.getDescription());
        dto.setButtonText(e.getButtonText());
        dto.setButtonLink(e.getButtonLink());

        if (e.getStats() != null) {
            dto.setStats(
                    e.getStats().stream()
                            .map(stat -> {
                                HomePageData.Stat s = new HomePageData.Stat();
                                s.setValue(stat.getValue());
                                s.setLabel(stat.getLabel());
                                return s;
                            })
                            .collect(Collectors.toList())
            );
        }
        return dto;
    }

    private HomePageData.BrandBanner toBrandBannerDto(HomePageContent.BrandBanner e) {
        if (e == null) return null;

        HomePageData.BrandBanner dto = new HomePageData.BrandBanner();
        dto.setTitle(e.getTitle());
        dto.setSubtitle(e.getSubtitle());

        if (e.getContacts() != null) {
            dto.setContacts(
                    e.getContacts().stream()
                            .map(c -> {
                                HomePageData.ContactInfo ci =
                                        new HomePageData.ContactInfo();
                                ci.setType(c.getType());
                                ci.setValue(c.getValue());
                                ci.setDisplayValue(c.getDisplayValue());
                                return ci;
                            })
                            .collect(Collectors.toList())
            );
        }
        return dto;
    }

    private HomePageData.ContactSection toContactDto(HomePageContent.ContactSection e) {
        if (e == null) return null;

        HomePageData.ContactSection dto = new HomePageData.ContactSection();
        dto.setCompanyName(e.getCompanyName());
        dto.setCompanySince(e.getCompanySince());
        dto.setReservationPhone(e.getReservationPhone());
        dto.setHotelPhone(e.getHotelPhone());
        dto.setVisitUs(e.getVisitUs());
        dto.setEmail(e.getEmail());
        dto.setWatsApp(e.getWatsApp());
        dto.setCorporateAddress(e.getCorporateAddress());
        dto.setSupportHours(e.getSupportHours());
        dto.setSocialLinks(e.getSocialLinks());

        return dto;
    }
}

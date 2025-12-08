package com.example.hotelbackend.service.impl;

import com.example.hotelbackend.dto.OfferDTO;
import com.example.hotelbackend.model.Offer;
import com.example.hotelbackend.model.OffersPageContent;
import com.example.hotelbackend.repository.OffersPageRepository;
import com.example.hotelbackend.service.OfferService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OfferServiceImpl implements OfferService {

    private final OffersPageRepository repository;

    @PostConstruct
    public void init() {
        if (repository.count() > 0) return;

        OffersPageContent content = new OffersPageContent();

        Offer o1 = new Offer();
        o1.setId(1);
        o1.setTitle("PERFECT STAYCATIONS – THIS JOYFUL SEASON");
        o1.setDesc("This holiday season, enjoy the perfect staycation with exclusive savings crafted just for you.");
        o1.setValidity("04 Dec 2025 – 11 Jan 2026");
        o1.setImg("/assets/g1.png");
        o1.setLoginBtn("LOGIN / JOIN");

        Offer o2 = new Offer();
        o2.setId(2);
        o2.setTitle("SUITE SURPRISES - MEMBER ONLY");
        o2.setDesc("Indulge in a stay that goes beyond the ordinary and experience enhanced comfort.");
        o2.setValidity("Round the Year");
        o2.setImg("/assets/g2.png");
        o2.setLoginBtn("LOGIN / JOIN");

        content.setOffers(List.of(o1, o2));

        repository.save(content);
    }

    @Override
    public List<OfferDTO> getOffers() {
        OffersPageContent content = repository.findFirstBy()
                .orElseGet(OffersPageContent::new);

        return content.getOffers() != null ?
                content.getOffers().stream().map(this::toDto).collect(Collectors.toList()) :
                List.of();
    }

    @Override
    public List<OfferDTO> updateOffers(List<OfferDTO> dtoList) {
        OffersPageContent entity = repository.findFirstBy()
                .orElseGet(OffersPageContent::new);

        List<Offer> offers = dtoList.stream()
                .map(this::toEntity)
                .collect(Collectors.toList());

        entity.setOffers(offers);

        repository.save(entity);

        return dtoList;
    }

    /* Helpers */

    private OfferDTO toDto(Offer e) {
        OfferDTO dto = new OfferDTO();
        dto.setId(e.getId());
        dto.setTitle(e.getTitle());
        dto.setDesc(e.getDesc());
        dto.setValidity(e.getValidity());
        dto.setImg(e.getImg());
        dto.setLoginBtn(e.getLoginBtn());
        return dto;
    }

    private Offer toEntity(OfferDTO dto) {
        Offer e = new Offer();
        e.setId(dto.getId());
        e.setTitle(dto.getTitle());
        e.setDesc(dto.getDesc());
        e.setValidity(dto.getValidity());
        e.setImg(dto.getImg());
        e.setLoginBtn(dto.getLoginBtn());
        return e;
    }
}


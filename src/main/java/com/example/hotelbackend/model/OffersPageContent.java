package com.example.hotelbackend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Data
@Document(collection = "offers_page_content")
public class OffersPageContent {

    @Id
    private String id;

    private List<Offer> offers;
}

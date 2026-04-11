package com.example.hotelbackend.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "agent_inventory_price")
public class AgentInventoryPrice {

    @Id
    private String id;

    private String agentId;

    private String hotelId;

    private String roomTypeId;

    private String date;


    /* Pricing */

    private double roomOnlyPrice;

    private double breakfastPrice;

    private double mealPrice;


    /* Status */

    private boolean active;
}
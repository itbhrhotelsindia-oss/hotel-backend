package com.example.hotelbackend.dto.agent;

import lombok.Data;

@Data
public class UpsertAgentPricingRequest {

    private String agentId;

    private String hotelId;

    private String roomTypeId;

    private String startDate;

    private String endDate;


    /* Prices */

    private double roomOnlyPrice;

    private double breakfastPrice;

    private double mealPrice;
}
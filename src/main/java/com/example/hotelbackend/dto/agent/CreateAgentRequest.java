package com.example.hotelbackend.dto.agent;

import lombok.Data;

import java.util.List;

@Data
public class CreateAgentRequest {

    private String username;

    private String password;

    private String companyName;

    private String contactPerson;

    private String email;

    private String mobileNumber;

    private List<String> hotelIds;

}

package com.example.hotelbackend.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "agents")
public class Agent {

    @Id
    private String id;

    /* Login Fields */

    private String username;

    private String password;

    private String role; // AGENT


    /* Company Details */

    private String companyName;

    private String contactPerson;

    private String email;

    private String mobileNumber;


    /* Access Control */

    private List<String> hotelIds;

    private boolean active;
}

package com.example.hotelbackend.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "owners")
public class Owner {

    @Id
    private String id;

    private String username;
    private String password;   // stored as HASH
    private String role;       // OWNER

    private List<String> hotelIds; // 🔑 linked hotels

    private boolean active;
}


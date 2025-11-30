package com.example.hotelbackend.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "hotels")
public class Hotel {
    @Id
    private String id;
    private String name;
    private String address;
    private String city;
    private String description;
    private List<Room> rooms;
    private List<String> images;
    private double rating;

    public Hotel() {}

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public List<Room> getRooms() { return rooms; }
    public void setRooms(List<Room> rooms) { this.rooms = rooms; }
    public List<String> getImages() { return images; }
    public void setImages(List<String> images) { this.images = images; }
    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }
}

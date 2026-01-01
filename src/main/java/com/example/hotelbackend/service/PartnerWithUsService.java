package com.example.hotelbackend.service;

import com.example.hotelbackend.model.PartnerWithUsEnquiry;
import com.example.hotelbackend.repository.PartnerWithUsRepository;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;


import java.time.LocalDateTime;
import java.util.List;

@Service
public class PartnerWithUsService {

    private final PartnerWithUsRepository repository;
    private final EmailService emailService;

    @Value("${app.owner.email}")
    private String ownerEmail;

    public PartnerWithUsService(
            PartnerWithUsRepository repository,
            EmailService emailService) {
        this.repository = repository;
        this.emailService = emailService;
    }

    public PartnerWithUsEnquiry submit(PartnerWithUsEnquiry enquiry) {

        enquiry.setCreatedAt(LocalDateTime.now());

        PartnerWithUsEnquiry saved = repository.save(enquiry);

        // 📧 SEND EMAIL TO OWNER
       sendOwnerNotification(saved);

        return saved;
    }

    private void sendOwnerNotification(PartnerWithUsEnquiry enquiry) {

        String subject = "New Partner With Us Enquiry";

        String body =
                "A new partnership enquiry has been received:\n\n" +
                        "Full Name: " + enquiry.getFullName() + "\n" +
                        "Phone: " + enquiry.getContactNumber() + "\n" +
                        "Email: " + (enquiry.getEmail() != null ? enquiry.getEmail() : "Not provided") + "\n" +
                        "Company: " + (enquiry.getCompanyName() != null ? enquiry.getCompanyName() : "Not provided") + "\n" +
                        "Partnership Type: " + (enquiry.getPartnershipType() != null ? enquiry.getPartnershipType() : "Not provided") + "\n" +
                        "Location: " + (enquiry.getLocation() != null ? enquiry.getLocation() : "Not provided") + "\n\n" +
                        "Message:\n" +
                        (enquiry.getMessage() != null ? enquiry.getMessage() : "Not provided");

        emailService.sendEmail(ownerEmail, subject, body);
    }

    // ADMIN APIs
    public List<PartnerWithUsEnquiry> getAll() {
        return repository.findAll();
    }

    public PartnerWithUsEnquiry getById(String id) {
        return repository.findById(id).orElse(null);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }
}


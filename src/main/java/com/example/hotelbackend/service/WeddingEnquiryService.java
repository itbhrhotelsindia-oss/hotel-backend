package com.example.hotelbackend.service;

import com.example.hotelbackend.model.WeddingEnquiry;
import com.example.hotelbackend.repository.WeddingEnquiryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class WeddingEnquiryService {

    private final WeddingEnquiryRepository repository;
    private final EmailService emailService;

    @Value("${app.owner.email}")
    private String ownerEmail;

    public WeddingEnquiryService(
            WeddingEnquiryRepository repository,
            EmailService emailService) {
        this.repository = repository;
        this.emailService = emailService;
    }


    public WeddingEnquiry submit(WeddingEnquiry enquiry) {

        enquiry.setPage("WEDDING");
        enquiry.setCreatedAt(LocalDateTime.now());

        WeddingEnquiry saved = repository.save(enquiry);

        // 📧 SEND OWNER EMAIL
       // sendOwnerNotification(saved);

        return saved;
    }


    // ADMIN APIs
    public List<WeddingEnquiry> getAll() {
        return repository.findAll();
    }

    public WeddingEnquiry getById(String id) {
        return repository.findById(id).orElse(null);
    }

    public void delete(String id) {
        repository.deleteById(id);
    }

    private void sendOwnerNotification(WeddingEnquiry enquiry) {

        String subject = "New Wedding Enquiry Received";

        String body =
                "A new wedding enquiry has been received:\n\n" +
                        "Name: " + enquiry.getName() + "\n" +
                        "Phone: " + enquiry.getPhone() + "\n" +
                        "Email: " + (enquiry.getEmail() != null ? enquiry.getEmail() : "Not provided") + "\n" +
                        "City: " + (enquiry.getCity() != null ? enquiry.getCity() : "Not provided") + "\n" +
                        "Query For: " + (enquiry.getQueryFor() != null ? enquiry.getQueryFor() : "Not provided") + "\n\n" +
                        "Comments:\n" +
                        (enquiry.getComments() != null ? enquiry.getComments() : "Not provided");

        try {
            emailService.sendEmail(ownerEmail, subject, body);
        } catch (Exception e) {
            // ❗ do NOT break submission if email fails
            System.err.println("Failed to send wedding enquiry email: " + e.getMessage());
        }
    }



}


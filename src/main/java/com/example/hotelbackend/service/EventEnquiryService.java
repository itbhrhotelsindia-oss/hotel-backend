package com.example.hotelbackend.service;

import com.example.hotelbackend.model.EventEnquiry;
import com.example.hotelbackend.repository.EventEnquiryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class EventEnquiryService {

    private final EventEnquiryRepository repository;
    private final EmailService emailService;

    @Value("${app.owner.email}")
    private String ownerEmail;

    public EventEnquiryService(
            EventEnquiryRepository repository,
            EmailService emailService) {
        this.repository = repository;
        this.emailService = emailService;
    }

    public EventEnquiry submit(EventEnquiry enquiry) {

        enquiry.setCreatedAt(LocalDateTime.now());
        enquiry.setEnquiryType("EVENT");

        EventEnquiry saved = repository.save(enquiry);

        // 📧 Send email to owner
        sendOwnerNotification(saved);

        return saved;
    }


    // GET ALL
    public List<EventEnquiry> getAll() {
        return repository.findAll();
    }

    // GET ONE
    public EventEnquiry getOne(String id) {
        return repository.findById(id).orElse(null);
    }

    // DELETE
    public void delete(String id) {
        repository.deleteById(id);
    }

    private void sendOwnerNotification(EventEnquiry enquiry) {

        String subject = "New Event Enquiry Received";

        String body =
                "A new event enquiry has been received:\n\n" +
                        "Name: " + enquiry.getName() + "\n" +
                        "Phone: " + enquiry.getPhone() + "\n" +
                        "Email: " + (enquiry.getEmail() != null ? enquiry.getEmail() : "Not provided") + "\n" +
                        "Event Type: " + enquiry.getEventType() + "\n" +
                        "Location: " + enquiry.getLocation() + "\n\n" +
                        "Query:\n" +
                        (enquiry.getQuery() != null ? enquiry.getQuery() : "Not provided");

        try {
            emailService.sendEmail(ownerEmail, subject, body);
        } catch (Exception e) {
            // ❗ Do not break enquiry flow if email fails
            System.err.println("Failed to send event enquiry email: " + e.getMessage());
        }
    }

}


package com.example.hotelbackend.service;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    @Value("${SENDGRID_API_KEY}")
    private String sendGridApiKey;

    @Value("${EMAIL_FROM}")
    private String fromEmail;

    @Value("${OWNER_EMAIL}")
    private String ownerEmail;

    /**
     * Send plain text email using SendGrid
     */
    public void sendEmail(String to, String subject, String body) {

        log.info("=== EMAIL SEND START ===");
        log.info("To Email: {}", to);
        log.info("From Email: {}", fromEmail);
        log.info("Subject: {}", subject);
        log.info("SendGrid API Key loaded: {}", sendGridApiKey != null && !sendGridApiKey.isBlank());

        Email from = new Email(fromEmail);
        Email toEmail = new Email(to);
        Content content = new Content("text/plain", body);
        Mail mail = new Mail(from, subject, toEmail, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sg.api(request);

            log.info("SendGrid Status Code: {}", response.getStatusCode());

            if (response.getStatusCode() >= 400) {
                log.error("SendGrid Error Response Body: {}", response.getBody());
                log.error("SendGrid Error Headers: {}", response.getHeaders());
            } else {
                log.info("Email accepted by SendGrid successfully");
            }

        } catch (IOException e) {
            log.error("IOException while sending email via SendGrid", e);
            throw new RuntimeException("Failed to send email via SendGrid", e);
        }

        log.info("=== EMAIL SEND END ===");
    }

    /**
     * Notify owner (helper method)
     */
    public void notifyOwner(String subject, String message) {
        log.info("Notifying owner at {}", ownerEmail);
        sendEmail(ownerEmail, subject, message);
    }
}

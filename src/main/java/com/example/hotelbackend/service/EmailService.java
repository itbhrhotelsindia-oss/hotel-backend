package com.example.hotelbackend.service;

import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class EmailService {

    @Value("${SENDGRID_API_KEY}")
    private String sendGridApiKey;

    @Value("${EMAIL_FROM}")
    private String fromEmail;

    @Value("${OWNER_EMAIL}")
    private String ownerEmail;

    /**
     * Send plain text email
     */
    public void sendEmail(String to, String subject, String body) {

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

            // Optional logging
            System.out.println("SendGrid Status Code: " + response.getStatusCode());

        } catch (IOException e) {
            throw new RuntimeException("Failed to send email via SendGrid", e);
        }
    }

    /**
     * Convenience method to notify owner
     */
    public void notifyOwner(String subject, String message) {
        sendEmail(ownerEmail, subject, message);
    }
}

package com.example.hotelbackend.service;
import com.example.hotelbackend.dto.legal.LegalPageResponse;
import com.example.hotelbackend.dto.legal.LegalSection;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LegalService {

    public LegalPageResponse getTermsAndConditions() {
        return new LegalPageResponse(
                "terms-and-conditions",
                "Terms and Conditions",
                "2025-01-01",
                List.of(
                        new LegalSection(
                                "Business Information",
                                "This website www.bhrhotelsindia.com is owned and operated by BHR Hotels India LLP, registered at Plot No GH-4B, Arihant Abode, Greater Noida, Uttar Pradesh – 201310, India."
                        ),
                        new LegalSection(
                                "Contact Details",
                                "Email: bhrhotelsindia@gmail.com | Phone: 9211283334"
                        ),
                        new LegalSection(
                                "Nature of Services",
                                "We provide hotel booking, room reservation, and hospitality-related services."
                        ),
                        new LegalSection(
                                "User Obligations",
                                "Users must provide accurate information and use the platform lawfully."
                        ),
                        new LegalSection(
                                "Payments",
                                "Payments are securely processed via authorized payment gateways."
                        ),
                        new LegalSection(
                                "Limitation of Liability",
                                "We are not liable for downtime or third-party service failures."
                        ),
                        new LegalSection(
                                "Intellectual Property",
                                "All website content is owned by BHR Hotels India LLP."
                        ),
                        new LegalSection(
                                "Governing Law",
                                "Governed by Indian law with jurisdiction in Uttar Pradesh."
                        ),
                        new LegalSection(
                                "Grievance Officer",
                                "Preetam Chaubey | Email: bhrhotelsindia@gmail.com | Phone: 9211283334 | Mon–Fri (9 AM – 6 PM)"
                        )
                )
        );
    }

    public LegalPageResponse getPrivacyPolicy() {
        return new LegalPageResponse(
                "privacy-policy",
                "Privacy Policy",
                "2025-01-01",
                List.of(
                        new LegalSection(
                                "Introduction",
                                "This policy explains how we collect and protect your personal data."
                        ),
                        new LegalSection(
                                "Information We Collect",
                                "Name, phone, email, booking details, and payment reference IDs only."
                        ),
                        new LegalSection(
                                "Use of Information",
                                "Used for bookings, confirmations, support, and legal compliance."
                        ),
                        new LegalSection(
                                "Sharing of Information",
                                "Shared only with payment gateways and legal authorities."
                        ),
                        new LegalSection(
                                "Data Security",
                                "We apply reasonable security practices."
                        ),
                        new LegalSection(
                                "User Rights",
                                "You may request access, correction, or deletion of your data."
                        ),
                        new LegalSection(
                                "Consent",
                                "Using the website implies consent to this policy."
                        ),
                        new LegalSection(
                                "Grievance Officer",
                                "Preetam Chaubey | Email: bhrhotelsindia@gmail.com | Phone: 9211283334"
                        )
                )
        );
    }

    public LegalPageResponse getRefundPolicy() {
        return new LegalPageResponse(
                "refund-and-cancellation-policy",
                "Refund and Cancellation Policy",
                "2025-01-01",
                List.of(
                        new LegalSection(
                                "Booking Cancellation",
                                "Cancellations are allowed as per the booking terms."
                        ),
                        new LegalSection(
                                "Refund Policy",
                                "Eligible refunds are processed within 5–7 business days."
                        ),
                        new LegalSection(
                                "Non-Refundable Cases",
                                "No-shows and late cancellations are non-refundable."
                        ),
                        new LegalSection(
                                "Partial Refunds",
                                "Partial refunds are clearly communicated during booking."
                        ),
                        new LegalSection(
                                "Refund Queries",
                                "Email: bhrhotelsindia@gmail.com | Phone: 9211283334"
                        )
                )
        );
    }
}


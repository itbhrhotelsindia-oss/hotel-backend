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
                                "Legal Notice",
                                "This document is an electronic record in terms of the Information Technology Act, 2000 and the rules made thereunder. This document does not require any physical or digital signatures."
                        ),
                        new LegalSection(
                                "Business Information",
                                "This website www.hotaality.com is owned and operated by Hotaality Group of Hotels - A Brand of Hotaality RevTech Private Limited , a limited liability partnership having its registered office at 898, 8th Floor, Gaur City Mall, Sector-4, Greater Noida West, Gautam Buddha Nagar - 201306,India."
                        ),
                        new LegalSection(
                                "Contact Details",
                                "Email:  info@hotaality.com | Phone: 9211283334"
                        ),
                        new LegalSection(
                                "Nature of Services",
                                "Hotaality Group of Hotels - A Brand of Hotaality RevTech Private Limited  provides hotel booking, room reservation, and hospitality-related services through its website."
                        ),
                        new LegalSection(
                                "User Obligations",
                                "Users agree to provide accurate information, use the platform lawfully, and not engage in fraudulent or unauthorized activities."
                        ),
                        new LegalSection(
                                "Payments",
                                "All payments are processed securely through authorized payment gateways. Prices are displayed before payment, and successful payment confirms the booking as per the details shown at checkout."
                        ),
                        new LegalSection(
                                "Limitation of Liability",
                                "Hotaality Group of Hotels - A Brand of Hotaality RevTech Private Limited  shall not be liable for website downtime, third-party service failures, or circumstances beyond reasonable control."
                        ),
                        new LegalSection(
                                "Indemnity",
                                "Users agree to indemnify and hold harmless Hotaality Group of Hotels - A Brand of Hotaality RevTech Private Limited  from any claims arising due to misuse of the platform or violation of these terms."
                        ),
                        new LegalSection(
                                "Force Majeure",
                                "The company shall not be liable for failure to perform obligations due to events beyond reasonable control including natural disasters, government actions, or technical failures."
                        ),
                        new LegalSection(
                                "Intellectual Property",
                                "All content on this website, including text, graphics, and design, is the intellectual property of Hotaality Group of Hotels - A Brand of Hotaality RevTech Private Limited ."
                        ),
                        new LegalSection(
                                "Governing Law and Jurisdiction",
                                "These terms shall be governed by the laws of India, with exclusive jurisdiction of the courts of Uttar Pradesh, India."
                        ),
                        new LegalSection(
                                "Grievance Officer",
                                "Designation: Grievance Officer | Company: Hotaality Group of Hotels - A Brand of Hotaality RevTech Private Limited  | Email:  info@hotaality.com | Phone: 9211283334 | Working Hours: Mon–Fri (9 AM – 6 PM)"
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
                                "This Privacy Policy describes how Hotaality Group of Hotels - A Brand of Hotaality RevTech Private Limited  collects, uses, and protects your personal data. Our services are offered only within India, and data is processed in accordance with applicable Indian laws."
                        ),
                        new LegalSection(
                                "Information We Collect",
                                "We may collect name, phone number, email address, booking details, and payment reference identifiers. We do not store card, UPI, or banking details."
                        ),
                        new LegalSection(
                                "Use of Information",
                                "Information is used to process bookings, send confirmations, provide customer support, and comply with legal obligations."
                        ),
                        new LegalSection(
                                "Sharing of Information",
                                "Information may be shared only with authorized payment gateways and legal or regulatory authorities when required by law."
                        ),
                        new LegalSection(
                                "Data Security",
                                "We implement reasonable security practices to protect personal data from unauthorized access or disclosure."
                        ),
                        new LegalSection(
                                "Data Retention and Deletion",
                                "Personal data is retained only as long as necessary for business or legal purposes and may be deleted upon request, subject to applicable laws."
                        ),
                        new LegalSection(
                                "User Rights",
                                "Users may request access, correction, or deletion of their personal data by contacting us."
                        ),
                        new LegalSection(
                                "Consent",
                                "By using this website, you consent to the collection and use of information in accordance with this Privacy Policy."
                        ),
                        new LegalSection(
                                "Grievance Officer",
                                "Designation: Grievance Officer | Company: Hotaality Group of Hotels - A Brand of Hotaality RevTech Private Limited  | Email:  info@hotaality.com | Phone: 9211283334"
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
                                "Policy Overview",
                                "This Refund and Cancellation Policy is issued by Hotaality Group of Hotels - A Brand of Hotaality RevTech Private Limited ."
                        ),
                        new LegalSection(
                                "Booking Cancellation",
                                "Cancellations are allowed as per the cancellation terms displayed at the time of booking."
                        ),
                        new LegalSection(
                                "Refund Policy",
                                "Eligible refunds will be processed to the original payment method within 5–7 business days after approval."
                        ),
                        new LegalSection(
                                "Non-Refundable Cases",
                                "No-shows and cancellations made outside the permitted cancellation window are non-refundable."
                        ),
                        new LegalSection(
                                "Partial Refunds",
                                "Partial refunds, if applicable, are clearly communicated at the time of booking."
                        ),
                        new LegalSection(
                                "Refund Queries",
                                "Email:info@hotaality.com | Phone: 9211283334"
                        )
                )
        );
    }
}

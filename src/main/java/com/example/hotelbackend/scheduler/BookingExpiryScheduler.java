package com.example.hotelbackend.scheduler;

import com.example.hotelbackend.model.Booking;
import com.example.hotelbackend.repository.BookingRepository;
import com.example.hotelbackend.service.InventoryRollbackService;
import com.example.hotelbackend.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BookingExpiryScheduler {

    private final BookingRepository bookingRepository;
    private final InventoryRollbackService rollbackService;
    private final EmailService emailService;

    /**
     * Runs every 2 minutes
     */
    @Scheduled(cron = "0 */2 * * * *")
    public void cancelExpiredBookings() {

        LocalDateTime now = LocalDateTime.now();

        List<Booking> expiredBookings =
                bookingRepository.findByStatusAndPaymentExpiresAtBefore(
                        "PENDING",
                        now
                );

        for (Booking booking : expiredBookings) {

            // ❌ cancel booking
            booking.setStatus("CANCELLED");
            booking.setCancelledAt(now);
            booking.setCancelReason("PAYMENT_TIMEOUT");

            bookingRepository.save(booking);

            // 🔁 rollback inventory
            rollbackService.rollbackInventory(booking);

            // 📧 optional notification
            sendExpiryEmail(booking);
        }
    }

    private void sendExpiryEmail(Booking booking) {

        String subject =
                "Booking Cancelled Due to Payment Timeout | " +
                        booking.getBookingId();

        String body =
                "Dear " + booking.getGuestName() + ",\n\n" +
                        "Your booking has been cancelled because payment was not completed " +
                        "within the allowed time.\n\n" +
                        "Booking ID: " + booking.getBookingId() + "\n" +
                        "Status: CANCELLED\n\n" +
                        "You may create a new booking anytime.\n\n" +
                        "Regards,\nBHR Hotels India";

        emailService.sendEmail(
                booking.getGuestEmail(),
                subject,
                body
        );
    }
}

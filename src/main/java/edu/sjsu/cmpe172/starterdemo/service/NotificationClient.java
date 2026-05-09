package edu.sjsu.cmpe172.starterdemo.service;
import edu.sjsu.cmpe172.starterdemo.model.NotificationRequest;
import edu.sjsu.cmpe172.starterdemo.model.NotificationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

// Calls the mock notification service over HTTP
@Service
public class NotificationClient {

    private static final Logger logger = LoggerFactory.getLogger(NotificationClient.class);

    private final RestClient restClient;

    public NotificationClient() {
        // Points to the mock notification service running on the same server
        // In production, this URL would point to an external service
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8080")
                .build();
    }

    // Sends a booking confirmation notification through the notification service
    // makes an HTTP POST to /api/notifications/send with all the appointment details
    public NotificationResponse sendBookingConfirmation(int appointmentId, String recipientEmail,
                                                        String recipientName, String serviceName,
                                                        String slotTime) {
        NotificationRequest request = new NotificationRequest(
                appointmentId,
                recipientEmail,
                recipientName,
                serviceName,
                slotTime,
                "BOOKING_CONFIRMATION"
        );

        try {
            NotificationResponse response = restClient.post()
                    .uri("/api/notifications/send")
                    .header("Content-Type", "application/json")
                    .body(request)
                    .retrieve()
                    .body(NotificationResponse.class);

            logger.info("Notification service responded: {} - {}",
                    response.getStatus(), response.getMessage());
            return response;




        } catch (Exception exception) {
            logger.error("Failed to reach notification service: {}", exception.getMessage());
            return new NotificationResponse("FAILED",
                    "Notification service unavailable: " + exception.getMessage());
        }
    }
}
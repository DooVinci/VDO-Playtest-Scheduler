package edu.sjsu.cmpe172.starterdemo.controller;
import edu.sjsu.cmpe172.starterdemo.model.NotificationRequest;
import edu.sjsu.cmpe172.starterdemo.model.NotificationResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * This pretensds to be a external service that sends notifications, eventually would be replaced by a separate app from a different host
 * Has one endpoint which accepts all the information in a single req
 */

@RestController
@RequestMapping("/api/notifications")
public class MockNotificationController {
    private static final Logger logger = LoggerFactory.getLogger(MockNotificationController.class);

    //Accepts a notification request and simulates sending. Logs details and returns confirmation.
    @PostMapping("/send")
    public NotificationResponse sendNotification(@RequestBody NotificationRequest request) {

        logger.info("========== MOCK NOTIFICATION SERVICE ==========");
        logger.info("Type:      {}", request.getMessageType());
        logger.info("To:        {} ({})", request.getRecipientName(), request.getRecipientEmail());
        logger.info("Appt ID:   {}", request.getAppointmentId());
        logger.info("Service:   {}", request.getServiceName());
        logger.info("Time:      {}", request.getSlotTime());
        logger.info("Status:    SENT (simulated)");
        logger.info("===============================================");

        return new NotificationResponse(
                "SENT",
                "Notification delivered to " + request.getRecipientEmail()
                        + " for appointment #" + request.getAppointmentId()
        );
    }
}
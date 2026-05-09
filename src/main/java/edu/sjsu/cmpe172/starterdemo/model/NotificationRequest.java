package edu.sjsu.cmpe172.starterdemo.model;

/**
 * DTO sent to mock notification service
 * its course grained so it has all the information needed in a single req
 */
public class NotificationRequest {

    private int appointmentId;
    private String recipientEmail;
    private String recipientName;
    private String serviceName;
    private String slotTime;
    private String messageType; // e.g., "BOOKING_CONFIRMATION", "CANCELLATION"

    public NotificationRequest() {}

    public NotificationRequest(int appointmentId, String recipientEmail, String recipientName,
                               String serviceName, String slotTime, String messageType) {
        this.appointmentId = appointmentId;
        this.recipientEmail = recipientEmail;
        this.recipientName = recipientName;
        this.serviceName = serviceName;
        this.slotTime = slotTime;
        this.messageType = messageType;
    }

    public int getAppointmentId() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }

    public String getRecipientEmail() { return recipientEmail; }
    public void setRecipientEmail(String recipientEmail) { this.recipientEmail = recipientEmail; }

    public String getRecipientName() { return recipientName; }
    public void setRecipientName(String recipientName) { this.recipientName = recipientName; }

    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }

    public String getSlotTime() { return slotTime; }
    public void setSlotTime(String slotTime) { this.slotTime = slotTime; }

    public String getMessageType() { return messageType; }
    public void setMessageType(String messageType) { this.messageType = messageType; }
}
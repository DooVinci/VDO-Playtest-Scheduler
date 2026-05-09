package edu.sjsu.cmpe172.starterdemo.model;

public class NotificationResponse {

    private String status;   // "SENT" or "FAILED"
    private String message;  // Human-readable confirmation

    public NotificationResponse() {}

    public NotificationResponse(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
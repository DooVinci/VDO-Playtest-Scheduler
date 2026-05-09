package edu.sjsu.cmpe172.starterdemo.model;
import java.time.LocalDateTime;

public class Appointment {

    private int appointmentId;
    private int slotId;
    private int userId;
    private String apptStatus;
    private LocalDateTime createdAt;
    private LocalDateTime canceledAt;
    private String userDisplayName;
    private String serviceName;
    private LocalDateTime slotStartTime;
    private LocalDateTime slotEndTime;

    public Appointment() {}

    public Appointment(int appointmentId, int slotId, int userId,
                       String apptStatus, LocalDateTime createdAt, LocalDateTime canceledAt) {
        this.appointmentId = appointmentId;
        this.slotId = slotId;
        this.userId = userId;
        this.apptStatus = apptStatus;
        this.createdAt = createdAt;
        this.canceledAt = canceledAt;
    }

    public int getAppointmentId() { return appointmentId; }
    public void setAppointmentId(int appointmentId) { this.appointmentId = appointmentId; }

    public int getSlotId() { return slotId; }
    public void setSlotId(int slotId) { this.slotId = slotId; }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getApptStatus() { return apptStatus; }
    public void setApptStatus(String apptStatus) { this.apptStatus = apptStatus; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getCanceledAt() { return canceledAt; }
    public void setCanceledAt(LocalDateTime canceledAt) { this.canceledAt = canceledAt; }

    public String getUserDisplayName() { return userDisplayName; }
    public void setUserDisplayName(String userDisplayName) { this.userDisplayName = userDisplayName; }

    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }

    public LocalDateTime getSlotStartTime() { return slotStartTime; }
    public void setSlotStartTime(LocalDateTime slotStartTime) { this.slotStartTime = slotStartTime; }

    public LocalDateTime getSlotEndTime() { return slotEndTime; }
    public void setSlotEndTime(LocalDateTime slotEndTime) { this.slotEndTime = slotEndTime; }
}
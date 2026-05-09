package edu.sjsu.cmpe172.starterdemo.model;
import java.time.LocalDateTime;

public class AvailabilitySlot {

    private int slotId;
    private int providerId;
    private int serviceId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String slotStatus;
    private String serviceName;
    private String providerHandle;

    public AvailabilitySlot() {}

    public AvailabilitySlot(int slotId, int providerId, int serviceId,
                            LocalDateTime startTime, LocalDateTime endTime, String slotStatus) {
        this.slotId = slotId;
        this.providerId = providerId;
        this.serviceId = serviceId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.slotStatus = slotStatus;
    }

    public int getSlotId() { return slotId; }
    public void setSlotId(int slotId) { this.slotId = slotId; }

    public int getProviderId() { return providerId; }
    public void setProviderId(int providerId) { this.providerId = providerId; }

    public int getServiceId() { return serviceId; }
    public void setServiceId(int serviceId) { this.serviceId = serviceId; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public String getSlotStatus() { return slotStatus; }
    public void setSlotStatus(String slotStatus) { this.slotStatus = slotStatus; }

    public String getServiceName() { return serviceName; }
    public void setServiceName(String serviceName) { this.serviceName = serviceName; }

    public String getProviderHandle() { return providerHandle; }
    public void setProviderHandle(String providerHandle) { this.providerHandle = providerHandle; }
}
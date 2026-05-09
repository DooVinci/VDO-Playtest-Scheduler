package edu.sjsu.cmpe172.starterdemo.service;
import edu.sjsu.cmpe172.starterdemo.model.AvailabilitySlot;
import edu.sjsu.cmpe172.starterdemo.repository.AvailabilitySlotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AvailabilitySlotService {

    private static final Logger logger = LoggerFactory.getLogger(AvailabilitySlotService.class);

    private final AvailabilitySlotRepository availabilitySlotRepository;

    public AvailabilitySlotService(AvailabilitySlotRepository availabilitySlotRepository) {
        this.availabilitySlotRepository = availabilitySlotRepository;
    }

    public List<AvailabilitySlot> getOpenSlots() {
        return availabilitySlotRepository.findAllOpen();
    }

    public List<AvailabilitySlot> getAllSlots() {
        return availabilitySlotRepository.findAll();
    }

    public AvailabilitySlot getSlotById(int slotId) {
        return availabilitySlotRepository.findById(slotId);
    }

    public void createSlot(int providerId, int serviceId,
                           LocalDateTime startTime, LocalDateTime endTime) {
        logger.info("Creating slot: providerId={}, serviceId={}, start={}, end={}",
                providerId, serviceId, startTime, endTime);
        availabilitySlotRepository.createSlot(providerId, serviceId, startTime, endTime);
        logger.info("Slot created successfully");
    }
}
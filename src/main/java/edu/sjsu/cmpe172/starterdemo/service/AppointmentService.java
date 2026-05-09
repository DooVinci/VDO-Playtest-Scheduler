package edu.sjsu.cmpe172.starterdemo.service;
import edu.sjsu.cmpe172.starterdemo.model.Appointment;
import edu.sjsu.cmpe172.starterdemo.model.AvailabilitySlot;
import edu.sjsu.cmpe172.starterdemo.repository.AppointmentRepository;
import edu.sjsu.cmpe172.starterdemo.repository.AvailabilitySlotRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AppointmentService {

    private static final Logger logger = LoggerFactory.getLogger(AppointmentService.class);

    private final AppointmentRepository appointmentRepository;
    private final AvailabilitySlotRepository slotRepository;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              AvailabilitySlotRepository slotRepository) {
        this.appointmentRepository = appointmentRepository;
        this.slotRepository = slotRepository;
    }

    // get all appointment for the listing appointment page
    public List<Appointment> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    // get a single appointment by id
    public Appointment getAppointmentById(int appointmentId) {
        return appointmentRepository.findById(appointmentId);
    }

    /**
     * 1. SELECT ... FOR UPDATE acquires an exclusive row lock on the slot
     * 2. Check if slot is still OPEN
     * 3. UPDATE slot status to BOOKED
     * 4. INSERT appointment record
     *
     * @Transactional wraps this in a single DB transaction
     * If any exception is thrown, the transaction rolls back
     */
    @Transactional
    public Appointment bookAppointment(int slotId, int userId) {
        logger.info("Booking attempt: slotId={}, userId={}", slotId, userId);

        // Lock the slot row (blocks other transactions on the same slot)
        AvailabilitySlot slot = slotRepository.findByIdForUpdate(slotId);

        if (slot == null) {
            logger.error("Slot not found: slotId={}", slotId);
            throw new IllegalArgumentException("Slot not found: " + slotId);
        }

        // Check if the slot is still open after acquiring the lock
        if (!"OPEN".equals(slot.getSlotStatus())) {
            logger.warn("Slot already booked: slotId={}, attemptedBy=userId={}", slotId, userId);
            throw new SlotAlreadyBookedException(
                    "Slot " + slotId + " is no longer available.");
        }

        // Update slot status to BOOKED
        slotRepository.updateStatus(slotId, "BOOKED");

        // Remove any canceled appointment for this slot so the UNIQUE(slot_id) allows the booking
        appointmentRepository.deleteCanceledBySlotId(slotId);

        // Insert the appointment record
        Appointment appointment = new Appointment();
        appointment.setSlotId(slotId);
        appointment.setUserId(userId);
        appointment.setApptStatus("BOOKED");
        appointment.setCreatedAt(LocalDateTime.now());

        Appointment saved = appointmentRepository.save(appointment);

        logger.info("Booking confirmed: appointmentId={}, slotId={}, userId={}",
                saved.getAppointmentId(), slotId, userId);

        return saved;
    }

    // Cancel an existing appointment, sets the appt status to CANCELED and reopens slot
    @Transactional
    public void cancelAppointment(int appointmentId) {
        logger.info("Cancel attempt: appointmentId={}", appointmentId);

        Appointment appointment = appointmentRepository.findById(appointmentId);
        if (appointment == null) {
            logger.error("Appointment not found: appointmentId={}", appointmentId);
            throw new IllegalArgumentException("Appointment not found: " + appointmentId);
        }

        if ("CANCELED".equals(appointment.getApptStatus())) {
            logger.warn("Appointment already canceled: appointmentId={}", appointmentId);
            throw new IllegalStateException("Appointment " + appointmentId + " is already canceled.");
        }

        appointmentRepository.cancelAppointment(appointmentId);

        slotRepository.updateStatus(appointment.getSlotId(), "OPEN");

        logger.info("Appointment canceled: appointmentId={}, slotId={} reopened",
                appointmentId, appointment.getSlotId());
    }
}
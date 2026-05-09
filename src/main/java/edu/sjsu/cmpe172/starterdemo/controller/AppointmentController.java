package edu.sjsu.cmpe172.starterdemo.controller;
import edu.sjsu.cmpe172.starterdemo.model.Appointment;
import edu.sjsu.cmpe172.starterdemo.model.AvailabilitySlot;
import edu.sjsu.cmpe172.starterdemo.model.NotificationResponse;
import edu.sjsu.cmpe172.starterdemo.service.AppointmentService;
import edu.sjsu.cmpe172.starterdemo.service.AvailabilitySlotService;
import edu.sjsu.cmpe172.starterdemo.service.NotificationClient;
import edu.sjsu.cmpe172.starterdemo.service.SlotAlreadyBookedException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import static edu.sjsu.cmpe172.starterdemo.controller.DateFormatter.*;

@RestController
public class AppointmentController {

    private final AppointmentService appointmentService;
    private final AvailabilitySlotService slotService;
    private final NotificationClient notificationClient;

    public AppointmentController(AppointmentService appointmentService,
                                 AvailabilitySlotService slotService,
                                 NotificationClient notificationClient) {
        this.appointmentService = appointmentService;
        this.slotService = slotService;
        this.notificationClient = notificationClient;
    }

    // View appointments

    @GetMapping(value = "/appointments", produces = "text/html")
    public String listAppointments() {
        List<Appointment> appointments = appointmentService.getAllAppointments();

        StringBuilder rows = new StringBuilder();
        for (Appointment appt : appointments) {
            String badgeClass = switch (appt.getApptStatus()) {
                case "BOOKED" -> "badge-booked";
                case "CANCELED" -> "badge-canceled";
                default -> "badge-open";
            };

            rows.append("<tr>")
                .append("<td>").append(appt.getAppointmentId()).append("</td>")
                .append("<td>").append(appt.getUserDisplayName()).append("</td>")
                .append("<td>").append(appt.getServiceName()).append("</td>")
                .append("<td>").append(formatDateTime(appt.getSlotStartTime())).append("</td>")
                .append("<td>").append(formatDateTime(appt.getSlotEndTime())).append("</td>")
                .append("<td><span class='badge ").append(badgeClass).append("'>")
                .append(appt.getApptStatus()).append("</span></td>")
                .append("<td>").append(formatDateTime(appt.getCreatedAt())).append("</td>")
                .append("<td>")
                .append("<a class='action-link' onclick=\"fetch('/appointments/")
                .append(appt.getAppointmentId())
                .append("/notify', {method:'POST'}).then(r=>r.text()).then(t=>alert(t));return false;\">")
                .append("Notify</a>")
                .append("<span class='action-sep'>|</span>")
                .append("<a class='action-link' onclick=\"fetch('/appointments/")
                .append(appt.getAppointmentId())
                .append("/cancel', {method:'POST'}).then(r=>r.text()).then(t=>{alert(t);location.reload();});return false;\">")
                .append("Cancel</a>")
                .append("</td>")
                .append("</tr>");
        }

        String tableContent;
        if (appointments.isEmpty()) {
            tableContent = "<div class='empty-state'>No appointments found.</div>";
        } else {
            tableContent = """
                <div class="data-table-wrapper">
                <table class="data-table">
                    <thead><tr>
                        <th>ID</th><th>Tester</th><th>Service</th>
                        <th>Start</th><th>End</th><th>Status</th>
                        <th>Booked At</th><th>Actions</th>
                    </tr></thead>
                    <tbody>%s</tbody>
                </table>
                </div>
                """.formatted(rows.toString());
        }

        return PageLayout.wrap("Appointments", """
            <div class="page-header">
                <h1>Appointments</h1>
                <p>All booked playtest sessions</p>
            </div>
            %s
            """.formatted(tableContent));
    }

    // Browsing available slots

    @GetMapping(value = "/slots", produces = "text/html")
    public String listSlots() {
        List<AvailabilitySlot> slots = slotService.getOpenSlots();

        StringBuilder rows = new StringBuilder();
        for (AvailabilitySlot slot : slots) {
            rows.append("<tr>")
                .append("<td>").append(slot.getSlotId()).append("</td>")
                .append("<td>").append(slot.getServiceName()).append("</td>")
                .append("<td>").append(slot.getProviderHandle()).append("</td>")
                .append("<td>").append(formatDateTime(slot.getStartTime())).append("</td>")
                .append("<td>").append(formatDateTime(slot.getEndTime())).append("</td>")
                .append("<td><span class='badge badge-open'>").append(slot.getSlotStatus()).append("</span></td>")
                .append("<td><a class='action-link' href='/book?slotId=").append(slot.getSlotId()).append("'>Book</a></td>")
                .append("</tr>");
        }

        String tableContent;
        if (slots.isEmpty()) {
            tableContent = "<div class='empty-state'>No open slots available.</div>";
        } else {
            tableContent = """
                <div class="data-table-wrapper">
                <table class="data-table">
                    <thead><tr>
                        <th>Slot</th><th>Service</th><th>Coordinator</th>
                        <th>Start</th><th>End</th><th>Status</th><th>Action</th>
                    </tr></thead>
                    <tbody>%s</tbody>
                </table>
                </div>
                """.formatted(rows.toString());
        }

        return PageLayout.wrap("Available Slots", """
            <div class="page-header">
                <h1>Available Slots</h1>
                <p>Open playtest sessions ready for booking</p>
            </div>
            %s
            """.formatted(tableContent));
    }

    // Book an appointment

    @GetMapping(value = "/book", produces = "text/html")
    public String bookForm(@RequestParam(value = "slotId", required = false) Integer slotId) {
        List<AvailabilitySlot> openSlots = slotService.getOpenSlots();

        StringBuilder options = new StringBuilder();
        for (AvailabilitySlot slot : openSlots) {
            String selected = (slotId != null && slotId == slot.getSlotId()) ? " selected" : "";
            options.append("<option value='").append(slot.getSlotId()).append("'").append(selected).append(">")
                   .append("Slot ").append(slot.getSlotId())
                   .append(" \u2014 ").append(slot.getServiceName())
                   .append(" \u2014 ").append(formatDateTime(slot.getStartTime()))
                   .append("</option>");
        }

        return PageLayout.wrap("Book a Session", """
            <div class="page-header">
                <h1>Book a Session</h1>
                <p>Reserve your seat in a playtest</p>
            </div>
            <div class="form-card">
                <label>Select Slot</label>
                <select id="slotId">%s</select>
                <label>Your User ID</label>
                <input type="text" id="userId" placeholder="e.g. 2" />
                <button class="btn-primary" type="button" onclick="submitBooking()">Confirm Booking</button>
                <div id="result"></div>
            </div>
            <script>
                function submitBooking() {
                    var slotId = document.getElementById('slotId').value;
                    var userId = document.getElementById('userId').value;
                    if (!slotId || !userId) { alert('Please fill in all fields.'); return; }
                    fetch('/book', {
                        method: 'POST',
                        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                        body: 'slotId=' + slotId + '&userId=' + userId
                    }).then(r => r.text()).then(t => {
                        document.getElementById('result').innerHTML = t;
                    });
                }
            </script>
            """.formatted(options.toString()));
    }

    @PostMapping(value = "/book", produces = "text/html")
    public String bookAppointment(@RequestParam("slotId") int slotId,
                                  @RequestParam("userId") int userId) {
        try {
            Appointment booked = appointmentService.bookAppointment(slotId, userId);

            notificationClient.sendBookingConfirmation(
                    booked.getAppointmentId(),
                    "tester@studio.com",
                    "User " + userId,
                    "Playtest Session",
                    "Slot " + slotId
            );

            return "<div class='result-msg'>"
                 + "Booking confirmed! Appointment ID: " + booked.getAppointmentId()
                 + "</div>";

        } catch (SlotAlreadyBookedException exception) {
            return errorMsg("This slot has already been booked by another tester. "
                          + "Please choose a different slot.");

        } catch (DuplicateKeyException exception) {
            return errorMsg("This slot has already been booked. "
                          + "Please go back and select a different slot.");

        } catch (IllegalArgumentException exception) {
            return errorMsg(exception.getMessage());

        } catch (Exception exception) {
            String rawMessage = exception.getMessage();
            if (rawMessage != null && rawMessage.contains("Duplicate entry")) {
                return errorMsg("This slot has already been booked. "
                              + "Please go back and select a different slot.");
            }
            return errorMsg("Something went wrong. Please try again.");
        }
    }

    // Cancel an appointment

    @PostMapping(value = "/appointments/{id}/cancel", produces = "text/html")
    public String cancelAppointment(@PathVariable("id") int appointmentId) {
        try {
            appointmentService.cancelAppointment(appointmentId);
            return "Appointment " + appointmentId + " has been canceled. The slot is now open again.";

        } catch (IllegalStateException exception) {
            return "This appointment has already been canceled.";

        } catch (IllegalArgumentException exception) {
            return "Appointment not found.";

        } catch (Exception exception) {
            return "Something went wrong while canceling. Please try again.";
        }
    }

    // Notify user

    @PostMapping(value = "/appointments/{id}/notify", produces = "text/html")
    public String notifyAppointment(@PathVariable("id") int appointmentId) {
        Appointment appointment;
        try {
            appointment = appointmentService.getAppointmentById(appointmentId);
        } catch (Exception exception) {
            return "Appointment not found.";
        }

        if (appointment == null) {
            return "Appointment not found.";
        }

        NotificationResponse response = notificationClient.sendBookingConfirmation(
                appointment.getAppointmentId(),
                "tester@studio.com",
                appointment.getUserDisplayName(),
                appointment.getServiceName(),
                formatDateTime(appointment.getSlotStartTime()) + " - " + formatTime(appointment.getSlotEndTime())
        );

        return "Notification " + response.getStatus() + ": " + response.getMessage();
    }

    private String errorMsg(String message) {
        return "<div class='result-msg' style='background:#fef2f2;border-color:#fecaca;color:#991b1b;'>"
             + message + "</div>";
    }
}
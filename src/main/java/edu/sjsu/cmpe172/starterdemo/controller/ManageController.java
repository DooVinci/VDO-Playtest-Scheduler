package edu.sjsu.cmpe172.starterdemo.controller;
import edu.sjsu.cmpe172.starterdemo.model.AvailabilitySlot;
import edu.sjsu.cmpe172.starterdemo.service.AvailabilitySlotService;
import org.springframework.web.bind.annotation.*;
import static edu.sjsu.cmpe172.starterdemo.controller.DateFormatter.*;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class ManageController {

    private final AvailabilitySlotService slotService;
    public ManageController(AvailabilitySlotService slotService) {
        this.slotService = slotService;
    }

    @GetMapping(value = "/manage", produces = "text/html")
    public String managePage() {
        List<AvailabilitySlot> allSlots = slotService.getAllSlots();

        StringBuilder rows = new StringBuilder();
        for (AvailabilitySlot slot : allSlots) {
            String badgeClass = switch (slot.getSlotStatus()) {
                case "OPEN" -> "badge-open";
                case "BOOKED" -> "badge-booked";
                default -> "badge-canceled";
            };
            rows.append("<tr>")
                .append("<td>").append(slot.getSlotId()).append("</td>")
                .append("<td>").append(slot.getServiceName()).append("</td>")
                .append("<td>").append(slot.getProviderHandle()).append("</td>")
                .append("<td>").append(formatDateTime(slot.getStartTime())).append("</td>")
                .append("<td>").append(formatDateTime(slot.getEndTime())).append("</td>")
                .append("<td><span class='badge ").append(badgeClass).append("'>")
                .append(slot.getSlotStatus()).append("</span></td>")
                .append("</tr>");
        }

        String tableContent;
        if (allSlots.isEmpty()) {
            tableContent = "<div class='empty-state'>No slots created yet.</div>";
        } else {
            tableContent = """
                <div class="data-table-wrapper">
                <table class="data-table">
                    <thead><tr>
                        <th>Slot</th><th>Service</th><th>Coordinator</th>
                        <th>Start</th><th>End</th><th>Status</th>
                    </tr></thead>
                    <tbody>%s</tbody>
                </table>
                </div>
                """.formatted(rows.toString());
        }

        return PageLayout.wrap("Manage Sessions", """
            <div class="page-header">
                <h1>Manage Playtest Sessions</h1>
                <p>Coordinator dashboard &mdash; create and view all slots</p>
            </div>

            <div class="form-card">
                <label>Service ID</label>
                <select id="serviceId">
                    <option value="1">1 &mdash; Functional QA</option>
                    <option value="2">2 &mdash; Performance QA</option>
                    <option value="3">3 &mdash; Enjoyability QA</option>
                    <option value="4">4 &mdash; Storyline QA</option>
                </select>
                <label>Provider ID</label>
                <input type="text" id="providerId" placeholder="e.g. 1" value="1" />
                <label>Start Time</label>
                <input type="text" id="startTime" placeholder="2026-07-01T09:00" />
                <label>End Time</label>
                <input type="text" id="endTime" placeholder="2026-07-01T10:00" />
                <button class="btn-primary" type="button" onclick="createSlot()">Create Slot</button>
                <div id="result"></div>
            </div>

            <div style="margin-top: 40px;">
                <h2 style="font-size: 22px; font-weight: 600; margin-bottom: 4px;">All Slots</h2>
                <p style="font-size: 14px; color: #86868b; margin-bottom: 12px;">Every slot across all statuses</p>
                %s
            </div>

            <script>
                function createSlot() {
                    var serviceId = document.getElementById('serviceId').value;
                    var providerId = document.getElementById('providerId').value;
                    var startTime = document.getElementById('startTime').value;
                    var endTime = document.getElementById('endTime').value;
                    if (!serviceId || !providerId || !startTime || !endTime) {
                        alert('Please fill in all fields.'); return;
                    }
                    fetch('/manage/create', {
                        method: 'POST',
                        headers: {'Content-Type': 'application/x-www-form-urlencoded'},
                        body: 'serviceId=' + serviceId + '&providerId=' + providerId
                            + '&startTime=' + startTime + '&endTime=' + endTime
                    }).then(r => r.text()).then(t => {
                        document.getElementById('result').innerHTML = t;
                        setTimeout(() => location.reload(), 1500);
                    });
                }
            </script>
            """.formatted(tableContent));
    }

    @PostMapping(value = "/manage/create", produces = "text/html")
    public String createSlot(@RequestParam("serviceId") int serviceId,
                             @RequestParam("providerId") int providerId,
                             @RequestParam("startTime") String startTime,
                             @RequestParam("endTime") String endTime) {
        try {
            LocalDateTime start = LocalDateTime.parse(startTime);
            LocalDateTime end = LocalDateTime.parse(endTime);

            if (end.isBefore(start) || end.isEqual(start)) {
                return "<div class='result-msg' style='background:#fef2f2;border-color:#fecaca;color:#991b1b;'>"
                     + "End time must be after start time.</div>";
            }

            slotService.createSlot(providerId, serviceId, start, end);
            return "<div class='result-msg'>Slot created successfully!</div>";

        } catch (Exception exception) {
            return "<div class='result-msg' style='background:#fef2f2;border-color:#fecaca;color:#991b1b;'>"
                 + "Failed to create slot. Check your inputs and try again.</div>";
        }
    }
}
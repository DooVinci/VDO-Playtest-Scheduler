package edu.sjsu.cmpe172.starterdemo.repository;
import edu.sjsu.cmpe172.starterdemo.model.Appointment;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class AppointmentRepository {

    private final JdbcTemplate jdbcTemplate;

    public AppointmentRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


     //Returns all appointments joined with user, slot, and service info
    public List<Appointment> findAll() {
        String sql = "SELECT a.appointment_id, a.slot_id, a.user_id, "
                   + "a.appt_status, a.created_at, a.canceled_at, "
                   + "u.display_name AS user_display_name, "
                   + "sv.service_name, "
                   + "s.start_time AS slot_start_time, "
                   + "s.end_time AS slot_end_time "
                   + "FROM appointments a "
                   + "JOIN users u ON a.user_id = u.user_id "
                   + "JOIN availability_slots s ON a.slot_id = s.slot_id "
                   + "JOIN services sv ON s.service_id = sv.service_id "
                   + "ORDER BY s.start_time DESC";
        return jdbcTemplate.query(sql, new AppointmentRowMapper());
    }

    // Find a single appointment by ID with join
    public Appointment findById(int appointmentId) {
        String sql = "SELECT a.appointment_id, a.slot_id, a.user_id, "
                   + "a.appt_status, a.created_at, a.canceled_at, "
                   + "u.display_name AS user_display_name, "
                   + "sv.service_name, "
                   + "s.start_time AS slot_start_time, "
                   + "s.end_time AS slot_end_time "
                   + "FROM appointments a "
                   + "JOIN users u ON a.user_id = u.user_id "
                   + "JOIN availability_slots s ON a.slot_id = s.slot_id "
                   + "JOIN services sv ON s.service_id = sv.service_id "
                   + "WHERE a.appointment_id = ?";
        return jdbcTemplate.queryForObject(sql, new AppointmentRowMapper(), appointmentId);
    }

    // Insert a new appointment and return it with the ID
    public Appointment save(Appointment appointment) {
        String sql = "INSERT INTO appointments (slot_id, user_id, appt_status, created_at) "
                   + "VALUES (?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setInt(1, appointment.getSlotId());
            preparedStatement.setInt(2, appointment.getUserId());
            preparedStatement.setString(3, appointment.getApptStatus());
            preparedStatement.setTimestamp(4, Timestamp.valueOf(appointment.getCreatedAt()));
            return preparedStatement;
        }, keyHolder);

        appointment.setAppointmentId(keyHolder.getKey().intValue());
        return appointment;
    }

    // Update the status and canceled_at timestamp for cancellation
    public void cancelAppointment(int appointmentId) {
        String sql = "UPDATE appointments SET appt_status = 'CANCELED', "
                   + "canceled_at = NOW() WHERE appointment_id = ?";
        jdbcTemplate.update(sql, appointmentId);
    }

    // deletes any cancaled appointments, clears the unique(slot_ID) constraint for rebooking
    public void deleteCanceledBySlotId(int slotId) {
        String sql = "DELETE FROM appointments WHERE slot_id = ? AND appt_status = 'CANCELED'";
        jdbcTemplate.update(sql, slotId);
    }

    // row mapper for appointment
    private static class AppointmentRowMapper implements RowMapper<Appointment> {
        @Override
        public Appointment mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
            Appointment appointment = new Appointment();
            appointment.setAppointmentId(resultSet.getInt("appointment_id"));
            appointment.setSlotId(resultSet.getInt("slot_id"));
            appointment.setUserId(resultSet.getInt("user_id"));
            appointment.setApptStatus(resultSet.getString("appt_status"));
            appointment.setCreatedAt(resultSet.getTimestamp("created_at").toLocalDateTime());

            Timestamp canceledTimestamp = resultSet.getTimestamp("canceled_at");
            if (canceledTimestamp != null) {
                appointment.setCanceledAt(canceledTimestamp.toLocalDateTime());
            }

            appointment.setUserDisplayName(resultSet.getString("user_display_name"));
            appointment.setServiceName(resultSet.getString("service_name"));
            appointment.setSlotStartTime(resultSet.getTimestamp("slot_start_time").toLocalDateTime());
            appointment.setSlotEndTime(resultSet.getTimestamp("slot_end_time").toLocalDateTime());
            return appointment;
        }
    }
}
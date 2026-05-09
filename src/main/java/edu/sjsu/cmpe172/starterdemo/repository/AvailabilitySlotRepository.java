package edu.sjsu.cmpe172.starterdemo.repository;
import edu.sjsu.cmpe172.starterdemo.model.AvailabilitySlot;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class AvailabilitySlotRepository {

    private final JdbcTemplate jdbcTemplate;

    public AvailabilitySlotRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<AvailabilitySlot> findAll() {
        String sql = "SELECT s.slot_id, s.provider_id, s.service_id, "
                   + "s.start_time, s.end_time, s.slot_status, "
                   + "sv.service_name, p.handle AS provider_handle "
                   + "FROM availability_slots s "
                   + "JOIN services sv ON s.service_id = sv.service_id "
                   + "JOIN providers p ON s.provider_id = p.provider_id "
                   + "ORDER BY s.start_time";
        return jdbcTemplate.query(sql, new AvailabilitySlotRowMapper());
    }

    public List<AvailabilitySlot> findAllOpen() {
        String sql = "SELECT s.slot_id, s.provider_id, s.service_id, "
                   + "s.start_time, s.end_time, s.slot_status, "
                   + "sv.service_name, p.handle AS provider_handle "
                   + "FROM availability_slots s "
                   + "JOIN services sv ON s.service_id = sv.service_id "
                   + "JOIN providers p ON s.provider_id = p.provider_id "
                   + "WHERE s.slot_status = 'OPEN' "
                   + "ORDER BY s.start_time";
        return jdbcTemplate.query(sql, new AvailabilitySlotRowMapper());
    }

    public AvailabilitySlot findById(int slotId) {
        String sql = "SELECT s.slot_id, s.provider_id, s.service_id, "
                   + "s.start_time, s.end_time, s.slot_status, "
                   + "sv.service_name, p.handle AS provider_handle "
                   + "FROM availability_slots s "
                   + "JOIN services sv ON s.service_id = sv.service_id "
                   + "JOIN providers p ON s.provider_id = p.provider_id "
                   + "WHERE s.slot_id = ?";
        return jdbcTemplate.queryForObject(sql, new AvailabilitySlotRowMapper(), slotId);
    }

    public AvailabilitySlot findByIdForUpdate(int slotId) {
        String sql = "SELECT s.slot_id, s.provider_id, s.service_id, "
                   + "s.start_time, s.end_time, s.slot_status, "
                   + "sv.service_name, p.handle AS provider_handle "
                   + "FROM availability_slots s "
                   + "JOIN services sv ON s.service_id = sv.service_id "
                   + "JOIN providers p ON s.provider_id = p.provider_id "
                   + "WHERE s.slot_id = ? "
                   + "FOR UPDATE";
        return jdbcTemplate.queryForObject(sql, new AvailabilitySlotRowMapper(), slotId);
    }

    public void updateStatus(int slotId, String newStatus) {
        String sql = "UPDATE availability_slots SET slot_status = ? WHERE slot_id = ?";
        jdbcTemplate.update(sql, newStatus, slotId);
    }

    // inserts new availability slots
    public void createSlot(int providerId, int serviceId,
                           java.time.LocalDateTime startTime, java.time.LocalDateTime endTime) {
        String sql = "INSERT INTO availability_slots (provider_id, service_id, start_time, end_time, slot_status) "
                   + "VALUES (?, ?, ?, ?, 'OPEN')";
        jdbcTemplate.update(sql, providerId, serviceId,
                Timestamp.valueOf(startTime), Timestamp.valueOf(endTime));
    }

    private static class AvailabilitySlotRowMapper implements RowMapper<AvailabilitySlot> {
        @Override
        public AvailabilitySlot mapRow(ResultSet resultSet, int rowNumber) throws SQLException {
            AvailabilitySlot slot = new AvailabilitySlot();
            slot.setSlotId(resultSet.getInt("slot_id"));
            slot.setProviderId(resultSet.getInt("provider_id"));
            slot.setServiceId(resultSet.getInt("service_id"));
            slot.setStartTime(resultSet.getTimestamp("start_time").toLocalDateTime());
            slot.setEndTime(resultSet.getTimestamp("end_time").toLocalDateTime());
            slot.setSlotStatus(resultSet.getString("slot_status"));
            slot.setServiceName(resultSet.getString("service_name"));
            slot.setProviderHandle(resultSet.getString("provider_handle"));
            return slot;
        }
    }
}
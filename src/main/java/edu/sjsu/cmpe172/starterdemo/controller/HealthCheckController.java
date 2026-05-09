package edu.sjsu.cmpe172.starterdemo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClient;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * app: whether the Spring Boot application is running
 * database: whether MySQL is reachable (real check via SELECT 1)
 * notificationService: whether the mock notification service responds (real HTTP ping)
 *
 * Returns 200 OK if all components are up
 * Returns 503 Service Unavailable if anyhing is down
 */
@RestController
public class HealthCheckController {

    private static final Logger logger = LoggerFactory.getLogger(HealthCheckController.class);

    private final JdbcTemplate jdbcTemplate;
    private final RestClient restClient;

    public HealthCheckController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.restClient = RestClient.builder()
                .baseUrl("http://localhost:8080")
                .build();
    }

    @GetMapping("/health")
    public ResponseEntity<Map<String, Object>> healthCheck() {

        Map<String, Object> health = new LinkedHashMap<>();
        health.put("timestamp", LocalDateTime.now().toString());

        boolean allHealthy = true;

        // if this code runs, the app is up
        health.put("app", "UP");

        // SELECT 1 query in SQL database for database check
        try {
            jdbcTemplate.queryForObject("SELECT 1", Integer.class);
            health.put("database", "UP");
        } catch (Exception exception) {
            health.put("database", "DOWN");
            health.put("databaseError", exception.getMessage());
            allHealthy = false;
            logger.error("Health check: database is DOWN - {}", exception.getMessage());
        }

        // pings mock notification endpoint
        try {
            restClient.post()
                    .uri("/api/notifications/send")
                    .header("Content-Type", "application/json")
                    .body("{\"appointmentId\":0,\"recipientEmail\":\"healthcheck@test\","
                        + "\"recipientName\":\"Health Check\",\"serviceName\":\"ping\","
                        + "\"slotTime\":\"ping\",\"messageType\":\"HEALTH_CHECK\"}")
                    .retrieve()
                    .body(String.class);
            health.put("notificationService", "UP");
        } catch (Exception exception) {
            health.put("notificationService", "DOWN");
            health.put("notificationServiceError", exception.getMessage());
            allHealthy = false;
            logger.error("Health check: notification service is DOWN - {}", exception.getMessage());
        }

        health.put("status", allHealthy ? "HEALTHY" : "DEGRADED");
        logger.info("Health check performed: status={}", allHealthy ? "HEALTHY" : "DEGRADED");

        if (allHealthy) {
            return ResponseEntity.ok(health);
        } else {
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(health);
        }
    }
}
package org.backend.config;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Slf4j
public class InjectDataConfig {

    private final JdbcTemplate jdbcTemplate;

    public InjectDataConfig(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void initializeData() {
        try {
            // Read the data.sql file from resources
            String dataSql = new BufferedReader(
                    new InputStreamReader(
                            Objects.requireNonNull(getClass().getResourceAsStream("/data.sql")),
                            StandardCharsets.UTF_8
                    )
            ).lines().collect(Collectors.joining("\n"));

            // Execute the SQL script
            jdbcTemplate.execute(dataSql);
        } catch (Exception e) {
            log.error("failed to inject data.sql!");
        }
    }
}

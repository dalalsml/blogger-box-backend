package com.dauphine.blogger.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class DbSchemaMigrationRunner implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(DbSchemaMigrationRunner.class);

    private final JdbcTemplate jdbcTemplate;

    public DbSchemaMigrationRunner(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void run(ApplicationArguments args) {
        String dataType = jdbcTemplate.query(
                """
                SELECT data_type
                FROM information_schema.columns
                WHERE table_schema = current_schema()
                  AND table_name = 'post'
                  AND column_name = 'content'
                """,
                rs -> rs.next() ? rs.getString("data_type") : null
        );

        if (dataType == null) {
            logger.warn("Schema migration skipped: column post.content not found.");
            return;
        }

        if (!"text".equalsIgnoreCase(dataType)) {
            logger.info("Applying schema migration: ALTER TABLE post ALTER COLUMN content TYPE TEXT");
            jdbcTemplate.execute("ALTER TABLE post ALTER COLUMN content TYPE TEXT");
            logger.info("Schema migration applied successfully.");
            return;
        }

        logger.info("Schema migration not needed: post.content is already TEXT.");
    }
}

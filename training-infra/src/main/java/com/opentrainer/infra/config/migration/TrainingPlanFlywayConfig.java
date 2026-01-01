package com.opentrainer.infra.config.migration;

import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.flyway.FlywayProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * Flyway configuration for Training-Plan database migrations.
 * Activated when application.data-source=training-plan
 */
@Slf4j
@Configuration
@ConditionalOnProperty(name = "application.data-source", havingValue = "training-plan")
public class TrainingPlanFlywayConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.flyway.training-plan")
    public FlywayProperties trainingPanFlywayProperties() {
        return new FlywayProperties();
    }

    @Bean(initMethod = "migrate")
    public Flyway trainingPanFlyway(@Qualifier("trainingPlanDataSource") DataSource dataSource) {
        FlywayProperties properties = trainingPanFlywayProperties();

        log.info("Configuring Flyway for Training-Plan database");
        log.info("Migration locations: {}", properties.getLocations());

        return Flyway.configure()
            .dataSource(dataSource)
            .locations(properties.getLocations().toArray(new String[0]))
            .baselineOnMigrate(properties.isBaselineOnMigrate())
            .baselineVersion(properties.getBaselineVersion())
            .schemas(properties.getSchemas().toArray(new String[0]))
            .load();
    }
}

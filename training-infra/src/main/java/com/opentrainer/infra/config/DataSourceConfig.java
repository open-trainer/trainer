package com.opentrainer.infra.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import jakarta.annotation.PostConstruct;

/**
 * Main configuration class that activates the appropriate data source
 * based on the application.data-source property.
 */
@Slf4j
@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(DataSourceProperties.class)
public class DataSourceConfig {

    private final DataSourceProperties dataSourceProperties;

    @PostConstruct
    public void init() {
        String activeDataSource = dataSourceProperties.getDataSource();
        log.info("Initializing with data source: {}", activeDataSource);

        if (activeDataSource == null) {
            throw new IllegalStateException(
                "application.data-source property must be set. Valid values: user, Training-Plan"
            );
        }

        if (!activeDataSource.equals("user") && !activeDataSource.equals("Training-Plan")) {
            throw new IllegalStateException(
                String.format("Invalid data source '%s'. Valid values: user, Training-Plan", activeDataSource)
            );
        }
    }
}

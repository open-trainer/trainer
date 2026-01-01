package com.opentrainer.infra.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Properties for configuring which data source to use.
 * Reads the 'application.data-source' property from application.yml.
 */
@Data
@Component
@ConfigurationProperties(prefix = "application")
public class DataSourceProperties {
    /**
     * Determines which data source configuration to activate.
     * Valid values: "user", "Training-Plan"
     */
    private String dataSource;
}

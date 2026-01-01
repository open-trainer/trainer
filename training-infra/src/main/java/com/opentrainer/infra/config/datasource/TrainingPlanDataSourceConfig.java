package com.opentrainer.infra.config.datasource;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Configuration for the Training-Plan database.
 * Activated when application.data-source=Training-Plan
 */
@Slf4j
@Configuration
@ConditionalOnProperty(name = "application.data-source", havingValue = "training-plan")
public class TrainingPlanDataSourceConfig {

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.training-plan")
    public DataSourceProperties trainingPanDataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource.training-plan.hikari")
    public DataSource trainingPlanDataSource() {
        log.info("Configuring training-plan DataSource");
        return trainingPanDataSourceProperties()
            .initializeDataSourceBuilder()
            .type(HikariDataSource.class)
            .build();
    }

    @Bean
    @Primary
    public LocalContainerEntityManagerFactoryBean trainingPlanEntityManagerFactory(DataSource trainingPlanDataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(trainingPlanDataSource);
        em.setPackagesToScan("com.opentrainer.domain.training");

        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);

        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", "validate");
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect");
        properties.put("hibernate.show_sql", false);
        properties.put("hibernate.format_sql", true);
        em.setJpaPropertyMap(properties);

        return em;
    }

    @Bean
    @Primary
    public PlatformTransactionManager trainingPanTransactionManager(
            LocalContainerEntityManagerFactoryBean trainingPlanEntityManagerFactory
    ) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(trainingPlanEntityManagerFactory.getObject());
        return transactionManager;
    }

    @Bean
    @Primary
    public JdbcTemplate trainingPanJdbcTemplate() {
        return new JdbcTemplate(trainingPlanDataSource());
    }
}

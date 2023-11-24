package org.example.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.logging.Logger;

@Configuration
public class DataSourceConfig {


    @Value("${spring.datasource.hikari.jdbc-url}")
    private String jdbcUrl;
    @Value("${spring.datasource.hikari.username}")
    private String username;
    @Value("${spring.datasource.hikari.password}")
    private String password;
    @Value("${spring.datasource.hikari.driver-class-name}")
    private String driverClassName;

    @Value("${spring.datasource.hikari.maximum-pool-size}")
    private int maximumPoolSize;

    @Value("${spring.datasource.hikari.minimum-idle}")
    private int minimumIdeal;
    @Value("${spring.datasource.hikari.connection-timeout}")
    private int connectionTimeOut;
    @Value("${spring.datasource.hikari.idle-timeout}")
    private int idealTimeOut;
    @Value("${spring.datasource.hikari.max-lifetime}")
    private int maxLifeTime;

    @Primary
    @Lazy
    @Scope("singleton")
    @Bean(name = "dataSource")

    public DataSource dataSource(){
        HikariDataSource config = new HikariDataSource();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(driverClassName);

        // HikariCP specific settings
        config.setMaximumPoolSize(maximumPoolSize);
        config.setMinimumIdle(minimumIdeal);
        config.setConnectionTimeout(connectionTimeOut);
        config.setIdleTimeout(idealTimeOut);
        config.setMaxLifetime(maxLifeTime);
        return config;

    }
}

package com.sso.app.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * 授权数据库数据源配置
 * 用于同步 Client 到 SAS 的 oauth2_registered_client 表
 */
@Configuration
public class AuthDataSourceConfig {

    @Bean("authDataSource")
    @ConfigurationProperties(prefix = "auth-datasource.hikari")
    public DataSource authDataSource() {
        return new HikariDataSource();
    }

    @Bean("authJdbcTemplate")
    public JdbcTemplate authJdbcTemplate() {
        return new JdbcTemplate(authDataSource());
    }
}

package com.akash.config;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class DataSourceConfig {
	@Primary
	@Bean
	@ConfigurationProperties(prefix = "spring.datasource")
	DataSource datasource() {
		return DataSourceBuilder.create().build();
	}
}

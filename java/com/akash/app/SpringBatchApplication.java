package com.akash.app;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableBatchProcessing
@EnableAsync
//@EnableScheduling
@ComponentScan(basePackages = {"com.akash.config","com.akash.services","com.akash.listener","com.akash.reader","com.akash.processor",
		"com.akash.writer","com.akash.controller"})
public class SpringBatchApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchApplication.class, args);
	}

}

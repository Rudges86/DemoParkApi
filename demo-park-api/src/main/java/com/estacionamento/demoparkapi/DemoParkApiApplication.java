package com.estacionamento.demoparkapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan(basePackages = {"com.estacionamento.demoparkapi.entity"})
@SpringBootApplication
public class DemoParkApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(DemoParkApiApplication.class, args);
	}

}

package ru.kuzmichev.SimpleBank.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication(scanBasePackages = "ru.kuzmichev.SimpleBank")
@EnableJpaRepositories(basePackages = "ru.kuzmichev.SimpleBank.server.service")
@EntityScan(basePackages = "ru.kuzmichev.SimpleBank.server.service")
public class SimpleBankApplication {

	public static void main(String[] args) {
		SpringApplication.run(SimpleBankApplication.class, args);
	}
}

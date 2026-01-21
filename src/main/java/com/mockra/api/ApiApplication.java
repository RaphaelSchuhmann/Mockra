package com.mockra.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mockra.api.config.ConfigLoader;

@SpringBootApplication
@RestController
public class ApiApplication {

	@GetMapping("/")
	void loadConfig() {
		ConfigLoader loader = new ConfigLoader();
		try {
			loader.loadAndValidate();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

}

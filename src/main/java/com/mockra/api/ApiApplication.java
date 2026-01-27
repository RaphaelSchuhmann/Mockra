package com.mockra.api;

import static com.mockra.api.errorHandling.ErrorHandler.displayMessage;

import java.nio.file.Path;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationEventPublisher;

import com.mockra.api.config.ConfigService;
import com.mockra.api.config.MockraConfig;
import com.mockra.api.errorHandling.ErrorType;

@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		Path path = Path.of(System.getProperty("user.dir"), "config.yaml");
		ApplicationEventPublisher publisher = event -> {};
		ConfigService tempService = new ConfigService(path, publisher);
		try {
			tempService.load(false, false);
		} catch (Exception ex) {
			displayMessage(ex.getMessage(), ErrorType.FATAL);
		}

		MockraConfig config = tempService.getConfig();

		SpringApplication app = new SpringApplication(ApiApplication.class);
		
		if (config.getServer() != null && config.getServer().getPort() != null) {
			app.setDefaultProperties(Map.of("server.port", config.getServer().getPort()));
		}
		app.run(args);
	}

}

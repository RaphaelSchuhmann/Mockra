package com.mockra.api.config;

import java.nio.file.Path;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigBeans {

    @Bean
    ConfigService configService(ApplicationEventPublisher publisher, @Value("${mockra.config.path:config.yaml}") String injectedPath) {
        Path path;

        if (injectedPath == null || injectedPath.isBlank()) {
            path = Path.of(System.getProperty("user.dir"), "config.yaml");
        } else {
            path = Path.of(injectedPath);
        }

        return new ConfigService(path, publisher);
    }
}

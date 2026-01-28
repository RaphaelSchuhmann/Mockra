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
        Path base = Path.of(System.getProperty("user.dir"));
        Path path = (injectedPath == null || injectedPath.isBlank()) 
            ? base.resolve("config.yaml") 
            : Path.of(injectedPath);
        if (!path.isAbsolute()) {
            path = base.resolve(path).normalize();
        }

        return new ConfigService(path, publisher);
    }
}

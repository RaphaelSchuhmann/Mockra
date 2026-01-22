package com.mockra.api.config;

import java.nio.file.Path;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigBeans {

    @Bean
    ConfigService configService() {
        Path path = Path.of(System.getProperty("user.dir"), "config.yaml");
        return new ConfigService(path);
    }
}

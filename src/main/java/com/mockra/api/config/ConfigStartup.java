package com.mockra.api.config;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigStartup {

    @Bean
    ApplicationRunner loadConfigOnStartup(ConfigService configService) {
        return args -> configService.load(false, false);
    }
}

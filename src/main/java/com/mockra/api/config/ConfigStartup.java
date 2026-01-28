package com.mockra.api.config;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigStartup {

    @Bean
    @ConditionalOnProperty(
        name = "mockra.config.autoload",
        havingValue = "true",
        matchIfMissing = true
    )
    ApplicationRunner loadConfigOnStartup(ConfigService configService) {
        return args -> configService.load(false, false);
    }
}

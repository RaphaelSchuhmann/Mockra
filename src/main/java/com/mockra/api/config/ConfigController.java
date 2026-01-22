package com.mockra.api.config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.mockra.api.errorHandling.ErrorHandler.displayMessage;

import com.mockra.api.config.MockraConfig.EndpointConfig;
import com.mockra.api.errorHandling.ErrorType;

@RestController
public class ConfigController {
    private final ConfigService configService;

    public ConfigController(ConfigService configService) {
        this.configService = configService;
    }

    @GetMapping("/admin/config/reload")
    public String hotReloadConfig() {
        try {
            configService.load(false);

            displayMessage("Config reloaded successfully!", ErrorType.INFO);
            return "Config reloaded successfully!";
        } catch (Exception e) {
            displayMessage(e.getMessage(), ErrorType.FATAL);
            return "Config reload failed!";
        }
    }

}

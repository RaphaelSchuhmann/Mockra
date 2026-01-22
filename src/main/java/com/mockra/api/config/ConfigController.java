package com.mockra.api.config;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.mockra.api.errorHandling.ErrorHandler.displayMessage;

import com.mockra.api.errorHandling.ErrorType;

@RestController
public class ConfigController {
    private final ConfigService configService;

    public ConfigController(ConfigService configService) {
        this.configService = configService;
    }

    @PostMapping("/admin/config/reload")
    public String hotReloadConfig() {
        try {
            configService.load(false);

            displayMessage("Config reloaded successfully!", ErrorType.INFO);
            return "Config reloaded successfully!";
        } catch (Exception e) {
            displayMessage(e.getMessage(), ErrorType.ERROR);
            return "Config reload failed!";
        }
    }

}

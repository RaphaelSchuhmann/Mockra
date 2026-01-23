package com.mockra.api.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<String> hotReloadConfig() {
        try {
            configService.load(false, true);

            displayMessage("Config reloaded successfully!", ErrorType.INFO);
            return ResponseEntity.ok("Config reloaded successfully!");
        } catch (Exception e) {
            displayMessage(e.getMessage(), ErrorType.ERROR);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Config reload failed!");
        }
    }

}

package com.mockra.api.config;

import static com.mockra.api.errorHandling.ErrorHandler.displayMessage;
import com.mockra.api.errorHandling.ErrorType;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Service;

import com.mockra.api.errorHandling.ConfigExceptions.IllegalConfigException;

@Service
public class ConfigService {
    private final AtomicReference<MockraConfig> activeConfig = new AtomicReference<>();

    public MockraConfig getConfig() { return activeConfig.get(); }

    public void load() {
        Path configPath = Path.of(System.getProperty("user.dir"), "config.yaml");
        try {
            MockraConfig newConfig = ConfigLoader.loadAndValidate(configPath);
            activeConfig.getAndSet(newConfig);
        } catch (Exception e) {
            // Hard exit on any exception thrown during config loading and validation
            displayMessage(e.getMessage(), ErrorType.FATAL);
        }
    }
}

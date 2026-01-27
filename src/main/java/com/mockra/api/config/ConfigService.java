package com.mockra.api.config;

import static com.mockra.api.errorHandling.ErrorHandler.displayMessage;
import com.mockra.api.errorHandling.ErrorType;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import com.mockra.api.errorHandling.ConfigExceptions.IllegalConfigException;
import com.mockra.api.events.ConfigLoadedEvent;

@Service
public class ConfigService {
    private final ApplicationEventPublisher publisher;
    private final AtomicReference<MockraConfig> activeConfig = new AtomicReference<>();
    private final Path configPath;

    public ConfigService(Path configPath, ApplicationEventPublisher publisher) {
        this.configPath = configPath;
        this.publisher = publisher;
    }

    public MockraConfig getConfig() { return activeConfig.get(); }

    public void load(boolean throwOnInvalid, boolean hotReload) throws IOException, IllegalConfigException {
        try {
            MockraConfig newConfig = ConfigLoader.loadAndValidate(configPath);
            activeConfig.set(newConfig);

            publisher.publishEvent(new ConfigLoadedEvent(newConfig, hotReload));
        } catch (IOException e) {
            if (throwOnInvalid) {
                throw e;
            } else {
                displayMessage(
                        "Error reading config:\n\tThere was an error while loading the config.yaml.\n\tPlease ensure that the config exists and is named config.yaml.",
                        hotReload ? ErrorType.ERROR : ErrorType.FATAL);
            }
        } catch (Exception e) {
            if (throwOnInvalid) {
                throw e;
            } else {
                displayMessage(e.getMessage(), hotReload ? ErrorType.ERROR : ErrorType.FATAL);
            }
        } 
    }
}

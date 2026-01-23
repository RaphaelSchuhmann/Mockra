package com.mockra.api.config;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.context.ApplicationEventPublisher;

import com.mockra.api.errorHandling.ConfigExceptions.IllegalConfigException;

import java.nio.file.Path;
import java.util.Objects;

public class ConfigServiceTests {

    @Test
    void loadingEmptyConfigShouldReturnNull() {
        ApplicationEventPublisher publisher = event -> {};
        ConfigService service = new ConfigService(Path.of("C:/"), publisher); // Path is irrelevant

        assertNull(service.getConfig());
    }

    @Test
    void validConfigIsLoaded() throws Exception {
        ApplicationEventPublisher publisher = event -> {};
        ConfigService service = new ConfigService(configPath("valid.yaml"), publisher);

        // Ensure activeConfig is null before any config is loaded
        assertNull(service.getConfig());

        service.load(true, false);

        MockraConfig config = service.getConfig();
        assertNotNull(config);
        assertEquals(8080, config.getServer().getPort());
    }

    @Test
    void invalidatesNullHttpMethod() throws Exception {
        ApplicationEventPublisher publisher = event -> {};
        ConfigService service = new ConfigService(configPath("invalid-null-method.yaml"), publisher);

        assertThrows(IllegalConfigException.class, () -> {
            service.load(true, false);
        });
    }

    @Test
    void invalidatesBlankId() throws Exception {
        ApplicationEventPublisher publisher = event -> {};
        ConfigService service = new ConfigService(configPath("invalid-blank-id.yaml"), publisher);

        assertThrows(IllegalConfigException.class, () -> {
            service.load(true, false);
        });
    }

    @Test
    void invalidatesEmptyResponses() throws Exception {
        ApplicationEventPublisher publisher = event -> {};
        ConfigService service = new ConfigService(configPath("invalid-empty-responses.yaml"), publisher);

        assertThrows(IllegalConfigException.class, () -> {
            service.load(true, false);
        });
    }

    @Test
    void invalidatesPortUnderMin() throws Exception {
        ApplicationEventPublisher publisher = event -> {};
        ConfigService service = new ConfigService(configPath("invalid-min-port.yaml"), publisher);

        assertThrows(IllegalConfigException.class, () -> {
            service.load(true, false);
        });
    }

    @Test
    void invalidatesPortOverMax() throws Exception {
        ApplicationEventPublisher publisher = event -> {};
        ConfigService service = new ConfigService(configPath("invalid-max-port.yaml"), publisher);

        assertThrows(IllegalConfigException.class, () -> {
            service.load(true, false);
        });
    }

    @Test
    void invalidatesHotReloadPath() throws Exception {
        ApplicationEventPublisher publisher = event -> {};
        ConfigService service = new ConfigService(configPath("invalid-hot-reload.yaml"), publisher);

        assertThrows(IllegalConfigException.class, () -> {
            service.load(true, false);
        });
    }

    @Test
    void invalidatesPathThatIsInGeneralReservedSpace() throws Exception {
        ApplicationEventPublisher publisher = event -> {};
        ConfigService service = new ConfigService(configPath("invalid-start-hot-reload-path.yaml"), publisher);

        assertThrows(IllegalConfigException.class, () -> {
            service.load(true, false);
        });
    }

    @Test
    void invalidatesRequestBodyForGET() throws Exception {
        ApplicationEventPublisher publisher = event -> {};
        ConfigService service = new ConfigService(configPath("invalid-body-get.yaml"), publisher);

        assertThrows(IllegalConfigException.class, () -> {
            service.load(true, false);
        });
    }

    @Test
    void invalidatesRequestBodyForPOST() throws Exception {
        ApplicationEventPublisher publisher = event -> {};
        ConfigService service = new ConfigService(configPath("invalid-body-post.yaml"), publisher);

        assertThrows(IllegalConfigException.class, () -> {
            service.load(true, false);
        });
    }

    private Path configPath(String name) throws Exception {
        return Path.of(
                Objects.requireNonNull(
                        getClass().getClassLoader().getResource("configs/" + name)).toURI());
    }
}

package com.mockra.api.config;

import org.junit.jupiter.api.Test;

import com.mockra.api.errorHandling.ConfigExceptions.IllegalConfigException;
import com.mockra.api.registry.EndpointRegistry;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.util.Objects;

public class ConfigServiceTests {

    @Test
    void loadingEmptyConfigShouldReturnNull() {
        ConfigService service = new ConfigService(Path.of("C:/")); // Path is irrelevant

        assertEquals(null, service.getConfig());
    }

    @Test
    void validConfigIsLoaded() throws Exception {
        ConfigService service = new ConfigService(configPath("valid.yaml"));

        // Ensure activeConfig is null before any config is loaded
        assertEquals(null, service.getConfig());

        service.load(true, false);

        MockraConfig config = service.getConfig();
        assertNotNull(config);
        assertEquals(8080, config.getServer().getPort());
    }

    @Test
    void invalidatesNullHttpMethod() throws Exception {
        ConfigService service = new ConfigService(configPath("invalid-null-method.yaml"));

        assertThrows(IllegalConfigException.class, () -> {
            service.load(true, false);
        });
    }

    @Test
    void invalidatesBlankId() throws Exception {
        ConfigService service = new ConfigService(configPath("invalid-blank-id.yaml"));

        assertThrows(IllegalConfigException.class, () -> {
            service.load(true, false);
        });
    }

    @Test
    void invalidatesEmptyResponses() throws Exception {
        ConfigService service = new ConfigService(configPath("invalid-empty-responses.yaml"));

        assertThrows(IllegalConfigException.class, () -> {
            service.load(true, false);
        });
    }

    @Test
    void invalidatesPortUnderMin() throws Exception {
        ConfigService service = new ConfigService(configPath("invalid-min-port.yaml"));

        assertThrows(IllegalConfigException.class, () -> {
            service.load(true, false);
        });
    }

    @Test
    void invalidatesPortOverMax() throws Exception {
        ConfigService service = new ConfigService(configPath("invalid-max-port.yaml"));

        assertThrows(IllegalConfigException.class, () -> {
            service.load(true, false);
        });
    }

    @Test
    void invalidatesHotReloadPath() throws Exception {
        ConfigService service = new ConfigService(configPath("invalid-hot-reload.yaml"));

        assertThrows(IllegalConfigException.class, () -> {
            service.load(true, false);
        });
    }

    @Test
    void invalidatesPathThatIsInGeneralReservedSpace() throws Exception {
        ConfigService service = new ConfigService(configPath("invalid-start-hot-reload-path.yaml"));

        assertThrows(IllegalConfigException.class, () -> {
            service.load(true, false);
        });
    }

    @Test
    void invalidatesRequestBodyForGET() throws Exception {
        ConfigService service = new ConfigService(configPath("invalid-body-get.yaml"));

        assertThrows(IllegalConfigException.class, () -> {
            service.load(true, false);
        });
    }

    @Test
    void invalidatesRequestBodyForPOST() throws Exception {
        ConfigService service = new ConfigService(configPath("invalid-body-post.yaml"));

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

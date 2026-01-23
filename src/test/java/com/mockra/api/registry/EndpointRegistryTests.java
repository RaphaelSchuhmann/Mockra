package com.mockra.api.registry;

import com.mockra.api.config.ConfigService;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationEventPublisher;

import com.mockra.api.config.*;

import java.nio.file.Path;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;

public class EndpointRegistryTests {

    @Test
    public void testRebuildRegistryBasicValidConfig() throws Exception {
        ApplicationEventPublisher publisher = event -> {};
        ConfigService service = new ConfigService(configPath("valid.yaml"), publisher);

        service.load(true, false);

        EndpointRegistry registry = new EndpointRegistry();
        registry.rebuildRegistry(service.getConfig());

        assertEquals("get", registry.getEndpoint("/api/get").getId());
        assertEquals(1, registry.size());
    }

    @Test
    public void testRebuildRegistryConfigMultipleEndpoints() throws Exception {
        ApplicationEventPublisher publisher = event -> {};
        ConfigService service = new ConfigService(configPath("valid-multiple-endpoints.yaml"), publisher);

        service.load(true, false);

        EndpointRegistry registry = new EndpointRegistry();
        registry.rebuildRegistry(service.getConfig());

        assertEquals("put", registry.getEndpoint("/api/put").getId());
        assertEquals(3, registry.size());
    }

    @Test
    public void testRebuildRegistryConfigUnknownPathIsNull() throws Exception {
        ApplicationEventPublisher publisher = event -> {};
        ConfigService service = new ConfigService(configPath("valid-multiple-endpoints.yaml"), publisher);

        service.load(true, false);

        EndpointRegistry registry = new EndpointRegistry();
        registry.rebuildRegistry(service.getConfig());

        assertNull(registry.getEndpoint("/api/delete"));
    }



    private Path configPath(String name) throws Exception {
        return Path.of(
                Objects.requireNonNull(
                        getClass().getClassLoader().getResource("configs/" + name)).toURI());
    }
}

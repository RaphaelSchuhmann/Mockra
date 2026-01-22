package com.mockra.api.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EndpointTests {
    
    @Test
    void testEndpointConstruction() {
        Endpoint endpoint = new Endpoint("test", "/api/test", HttpMethod.GET);
        endpoint.setMethod(HttpMethod.GET);

        assertNotNull(endpoint.getId(), "ID should be 'test'");
        assertEquals(HttpMethod.GET, endpoint.getMethod(), "Method should be set correctly");
        assertEquals("/api/test", endpoint.getPath(), "Path should be stored correctly");

        assertNotNull(endpoint.getVariants(), "Variants list should not be null");
        assertTrue(endpoint.getVariants().isEmpty(), "Variants list should be empty initially");

        EndpointVariant variant = new EndpointVariant("success", new ResponseDef(200));
        endpoint.addVariant(variant);

        assertEquals(1, endpoint.getVariants().size(), "Variants list should contain 1 variant");
        assertSame(variant, endpoint.getVariants().get(0), "Stored variant should match the added variant");
    }

    @Test
    void endpointConstructorShouldThrowIfNullIsPassed() {
        assertThrows(NullPointerException.class, () -> {
            new Endpoint("test", null, HttpMethod.GET);
        });

        assertThrows(NullPointerException.class, () -> {
            new Endpoint(null, "/api/test", HttpMethod.GET);
        });

        assertThrows(NullPointerException.class, () -> {
            new Endpoint("test", "/api/test", null);
        });

        assertThrows(NullPointerException.class, () -> {
            new Endpoint(null, null, null);
        });
    }

    @Test
    void endpointShouldAddNewVariant() {
        Endpoint endpoint = new Endpoint("test", "/api/test", HttpMethod.GET);
        EndpointVariant variant1 = new EndpointVariant("success", new ResponseDef(200)); 
        EndpointVariant variant2 = new EndpointVariant("error", new ResponseDef(500)); 

        endpoint.addVariant(variant1);
        endpoint.addVariant(variant2);

        assertNotNull(endpoint.getVariants(), "Variants list should be initialized");
        assertEquals(2, endpoint.getVariants().size(), "Variants list should contain 2 variants");
        assertSame(variant1, endpoint.getVariants().get(0), "First variant should match variant1");
        assertSame(variant2, endpoint.getVariants().get(1), "Second variant should match variant2");
    }

    @Test
    void endpointShouldNotAllowDuplicateVariants() {
        Endpoint endpoint = new Endpoint("test", "/api/test", HttpMethod.GET);
        EndpointVariant variant1 = new EndpointVariant("success", new ResponseDef(200));
        EndpointVariant variant2 = new EndpointVariant("success", new ResponseDef(200));

        endpoint.addVariant(variant1);
        endpoint.addVariant(variant2);

        assertEquals(1, endpoint.getVariants().size(), "Variants list should contain the variant once");
    }
}

package com.mockra.api.util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import com.mockra.api.model.*;

public class RequestContextTests {

    @Test
    public void testHeaderLookupShouldBeCaseInsensitive() {
        Map<String, String> headers = Map.of("X-Mockra-Variant", "foo");
        RequestContext ctx = new RequestContext(HttpMethod.GET, "/api/test", headers);
        assertEquals("foo", ctx.headers("x-mockra-variant").orElse(null));
        assertEquals("foo", ctx.headers("X-MOCKRA-VARIANT").orElse(null));
    }

    @Test
    public void testHeaderMissingShouldReturnEmpty() {
        RequestContext ctx = new RequestContext(HttpMethod.GET, "/api/test", Map.of());
        assertTrue(ctx.headers("x-mockra-variant").isEmpty());
    }

}

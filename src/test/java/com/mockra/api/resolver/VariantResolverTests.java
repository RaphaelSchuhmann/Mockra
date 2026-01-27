package com.mockra.api.resolver;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mockra.api.util.Result;
import com.mockra.api.util.RequestContext;
import com.mockra.api.model.*;

public class VariantResolverTests {

    @Test
    public void testResolvingOfValidVariant() {
        Endpoint endpoint = generateEndpoint(404, "not-found");

        // Generate request context
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("x-mockra-variant", "not-found");

        RequestContext context = new RequestContext(HttpMethod.GET, "/api/user", headersMap);
        
        Result<EndpointVariant, VariantResolverError> resolverResult = VariantResolver.resolve(endpoint, context);
        
        if (resolverResult instanceof Result.Ok<EndpointVariant, VariantResolverError> ok) {
            EndpointVariant resultVariant = ok.value();
            assertSame(endpoint.getVariants().get(0), resultVariant);
        } else if (resolverResult instanceof Result.Err<EndpointVariant, VariantResolverError> err) {
            fail("Expected Ok but got Err: " + err.error());
        }
    }

    @Test
    public void testMissingHeaderReturnsErr() {
        Endpoint endpoint = generateEndpoint(200, "success");

        // Generate request context
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("Content-Type", "application/json");

        RequestContext context = new RequestContext(HttpMethod.GET, "/api/user", headersMap);
        
        Result<EndpointVariant, VariantResolverError> resolverResult = VariantResolver.resolve(endpoint, context);

        if (resolverResult instanceof Result.Err<EndpointVariant, VariantResolverError> err) {
            assertEquals(err.error(), VariantResolverError.MISSING_VARIANT_HEADER);
        } else if (resolverResult instanceof Result.Ok<EndpointVariant, VariantResolverError> ok) {
            fail("Expected Err but got Ok: " + ok.value());
        }
    }

    @Test
    public void testEmptyHeaderReturnsErr() {
        Endpoint endpoint = generateEndpoint(400, "bad-request");

        // Generate request context
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("X-Mockra-Variant", "");

        RequestContext context = new RequestContext(HttpMethod.GET, "/api/user", headersMap);
        
        Result<EndpointVariant, VariantResolverError> resolverResult = VariantResolver.resolve(endpoint, context);

        if (resolverResult instanceof Result.Err<EndpointVariant, VariantResolverError> err) {
            assertEquals(err.error(), VariantResolverError.INVALID_VARIANT_HEADER);
        } else if (resolverResult instanceof Result.Ok<EndpointVariant, VariantResolverError> ok) {
            fail("Expected Err but got Ok: " + ok.value());
        }
    }

    @Test
    public void testUnknownVariantReturnsErr() {
        Endpoint endpoint = generateEndpoint(200, "success-user");

        // Generate request context
        Map<String, String> headersMap = new HashMap<>();
        headersMap.put("X-Mockra-Variant", "success");

        RequestContext context = new RequestContext(HttpMethod.GET, "/api/user", headersMap);
        
        Result<EndpointVariant, VariantResolverError> resolverResult = VariantResolver.resolve(endpoint, context);

        if (resolverResult instanceof Result.Err<EndpointVariant, VariantResolverError> err) {
            assertEquals(err.error(), VariantResolverError.UNKNOWN_VARIANT);
        } else if (resolverResult instanceof Result.Ok<EndpointVariant, VariantResolverError> ok) {
            fail("Expected Err but got Ok: " + ok.value());
        }       
    }

    private Endpoint generateEndpoint(int status, String variantValue) {
        // Generate new endoint
        ResponseDef respDef = new ResponseDef(status);        
        EndpointVariant variant = new EndpointVariant(variantValue, respDef);
        
        List<EndpointVariant> variants = new ArrayList<>();
        variants.add(variant);

        Endpoint endpoint = new Endpoint("user", "/api/user", HttpMethod.GET);
        endpoint.setVariants(variants);

        return endpoint;
    }

}

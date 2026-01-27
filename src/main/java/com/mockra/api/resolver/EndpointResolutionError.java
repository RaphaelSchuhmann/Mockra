package com.mockra.api.resolver;

public sealed interface EndpointResolutionError {
    record EndpointNotFound() implements EndpointResolutionError {}
    record VariantError(VariantResolverError cause) implements EndpointResolutionError {}
}

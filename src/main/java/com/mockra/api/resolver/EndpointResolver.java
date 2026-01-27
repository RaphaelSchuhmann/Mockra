package com.mockra.api.resolver;

import com.mockra.api.util.RequestContext;
import com.mockra.api.util.Result;

import org.springframework.stereotype.Component;

import com.mockra.api.model.Endpoint;
import com.mockra.api.model.EndpointVariant;

import com.mockra.api.registry.EndpointRegistry;

@Component
public class EndpointResolver {

    private final EndpointRegistry registry;

    public EndpointResolver(EndpointRegistry registry) {
        this.registry = registry;
    }

    public Result<EndpointVariant, EndpointResolutionError> resolve(RequestContext ctx) {
        Endpoint endpoint = registry.getEndpoint(ctx.path());

        if (endpoint == null) {
            return new Result.Err<>(new EndpointResolutionError.EndpointNotFound());
        }

        Result<EndpointVariant, VariantResolverError> res = VariantResolver.resolve(endpoint, ctx);

        if (res instanceof Result.Ok<EndpointVariant, VariantResolverError> ok) {
            return new Result.Ok<>(ok.value());
        }

        Result.Err<EndpointVariant, VariantResolverError> err = (Result.Err<EndpointVariant, VariantResolverError>) res;

        return new Result.Err<>(new EndpointResolutionError.VariantError(err.error()));
    }

}

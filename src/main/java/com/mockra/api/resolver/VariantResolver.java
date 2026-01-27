package com.mockra.api.resolver;

import com.mockra.api.util.RequestContext;
import com.mockra.api.util.Result;

import java.util.List;
import java.util.Optional;

import com.mockra.api.model.Endpoint;
import com.mockra.api.model.EndpointVariant;

public class VariantResolver {
    
    public static Result<EndpointVariant, VariantResolverError> resolve(Endpoint endpoint, RequestContext ctx) {
        Optional<String> headerOpt = ctx.headers("X-Mockra-Variant");

        if (headerOpt.isEmpty()) {
            return new Result.Err<>(VariantResolverError.MISSING_VARIANT_HEADER);
        }

        String mockraHeaderValue = headerOpt.get();

        if (mockraHeaderValue.isBlank()) {
            return new Result.Err<>(VariantResolverError.INVALID_VARIANT_HEADER);
        }

        List<EndpointVariant> variants = endpoint.getVariants();

        for (EndpointVariant variant : variants) {
            if (variant.getVariant().equals(mockraHeaderValue)) {
                return new Result.Ok<>(variant);
            }
        }

        return new Result.Err<>(VariantResolverError.UNKNOWN_VARIANT);
    }

}

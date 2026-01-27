package com.mockra.api.resolver;

public enum VariantResolverError {
    MISSING_VARIANT_HEADER("missing X-Mockra-Variant header"),
    INVALID_VARIANT_HEADER("variant header value is invalid"),
    UNKNOWN_VARIANT("variant value in header cannot be resolved");

    private final String message;

    VariantResolverError(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}

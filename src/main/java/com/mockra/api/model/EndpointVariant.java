package com.mockra.api.model;

import java.util.Objects;

public class EndpointVariant {
    private final String variant; // Required
    private final ResponseDef response; // Required

    public EndpointVariant(String variant, ResponseDef response) {
        this.variant = Objects.requireNonNull(variant, "variant is required");
        this.response = Objects.requireNonNull(response, "response is required");
    }

    public String getVariant() { return variant; }
    public ResponseDef getResponse() { return response; }
}

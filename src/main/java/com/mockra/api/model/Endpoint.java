package com.mockra.api.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Endpoint {
    private final String id = UUID.randomUUID().toString();
    private HttpMethod method;
    private final String path;
    private List<EndpointVariant> responses = new ArrayList<>();

    public Endpoint(String path) {
        this.path = Objects.requireNonNull(path);
    }

    public String getId() { return id; }

    public void setMethod(HttpMethod method) { this.method = method; } 
    public HttpMethod getMethod() { return method; }

    public String getPath() { return path; }

    public void addVariant(EndpointVariant response) { if (!responses.contains(response)) this.responses.add(response); }
    public List<EndpointVariant> getVariants() { return responses; }
}

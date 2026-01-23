package com.mockra.api.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

public class Endpoint {
    private final String id;
    private HttpMethod method;
    private final String path;
    private Map<String, FieldType> requestBody = new HashMap<>();
    private List<EndpointVariant> responses = new ArrayList<>();

    public Endpoint(String id, String path, HttpMethod method) {
        this.id = Objects.requireNonNull(id);
        this.path = Objects.requireNonNull(path);
        this.method = Objects.requireNonNull(method);
    }
    
    public String getId() { return id; }

    public void setMethod(HttpMethod method) { this.method = method; } 
    public HttpMethod getMethod() { return method; }

    public String getPath() { return path; }

    public Map<String, FieldType> getBody() { return Collections.unmodifiableMap(requestBody); }
    public void setBody(Map<String, FieldType> body) { this.requestBody = body; }

    public void setVariants(List<EndpointVariant> responses) { this.responses = responses;  }
    public void addVariant(EndpointVariant variant) { 
        boolean exists = responses.stream().anyMatch(v -> v.getVariant().equals(variant.getVariant()));

        if (!exists) responses.add(variant);
    }
    public List<EndpointVariant> getVariants() { return Collections.unmodifiableList(responses); }
}

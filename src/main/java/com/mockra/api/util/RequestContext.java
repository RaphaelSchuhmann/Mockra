package com.mockra.api.util;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import com.mockra.api.model.HttpMethod;

public record RequestContext(HttpMethod method, String path, Map<String, String> headers) {
    public RequestContext {
        headers = headers.entrySet().stream().collect(Collectors.toMap(e -> e.getKey().toLowerCase(), Map.Entry::getValue));
    }

    public Optional<String> headers(String name) {
        return Optional.ofNullable(headers.get(name.toLowerCase()));
    }
}

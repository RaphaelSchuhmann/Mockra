package com.mockra.api.util;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Locale;
import java.util.Objects;
import java.util.LinkedHashMap;

import com.mockra.api.model.HttpMethod;

public record RequestContext(HttpMethod method, String path, Map<String, String> headers) {
    public RequestContext {
        Objects.requireNonNull(headers, "headers");
        headers = headers.entrySet().stream().collect(Collectors.toMap(
            e -> Objects.requireNonNull(e.getKey(), "header name").toLowerCase(Locale.ROOT),
            Map.Entry::getValue,
            (a, b) -> b,
            LinkedHashMap::new
        ));
    }

    public Optional<String> headers(String name) {
        if (name == null) return Optional.empty();
        return Optional.ofNullable(headers.get(name.toLowerCase()));
    }
}

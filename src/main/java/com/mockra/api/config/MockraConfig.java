package com.mockra.api.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import com.mockra.api.model.HttpMethod;
import com.mockra.api.errorHandling.ConfigExceptions.*;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

@Data
public class MockraConfig {
    @NotNull public ServerConfig server;
    public List<EndpointConfig> endpoints = new ArrayList<>();

    @Data
    public static class ServerConfig {
        public int port;
    }

    @Data
    public static class EndpointConfig {
        @NotBlank(message = "Endpoint ID is required")
        public String id;

        @NotNull(message = "HTTP Method is required")
        public HttpMethod method;

        @NotBlank(message = "Path is required")
        public String path;

        public List<String> request = new ArrayList<>(); // Only needed when HttpMethod expects a body e.g. POST

        @NotEmpty(message = "Each endpoint must have at least one variant / response")
        public List<VariantConfig> responses = new ArrayList<>();

        public void validateBody() throws IllegalConfigException {
            if (method == HttpMethod.GET || method == HttpMethod.DELETE && !request.isEmpty()) {
                throw new IllegalConfigException("Endpoint '"+  id + "' error: GET or DELETE methods cannot have a request body.");
            } else if (method == HttpMethod.POST || method == HttpMethod.PUT || method == HttpMethod.PATCH && request.isEmpty()) {
                throw new IllegalConfigException("Endpoint '" + id + "' error: POST, PUT or PATCH methods need to have a request body.");
            }
        }
    }

    @Data
    public static class VariantConfig {
        @NotBlank(message = "Variant type is required")
        public String variant;

        @NotBlank(message = "Response status is required")
        public int status;
        
        public Map<String, String> body = new HashMap<>();
        public int delayMs;
    }
}

package com.mockra.api.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.List;
import com.mockra.api.model.HttpMethod;
import com.mockra.api.config.MockraConfig.EndpointConfig.ValidRequestBody;
import com.mockra.api.config.MockraConfig.EndpointConfig.NoHotReloadPath;
import com.mockra.api.errorHandling.ConfigExceptions.*;

import lombok.Data;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Data
public class MockraConfig {
    @NotNull(message = "Server config is required")
    @Valid
    public ServerConfig server;

    @NotEmpty(message = "Endpoint config is required")
    @Valid
    public List<EndpointConfig> endpoints = new ArrayList<>();

    @Data
    public static class ServerConfig {
        @NotNull(message = "Port is required")
        @Min(1)
        @Max(65535)
        public Integer port;
    }

    @Data
    @NoHotReloadPath
    @ValidRequestBody
    public static class EndpointConfig {
        @NotBlank(message = "Endpoint ID is required")
        public String id;

        @NotNull(message = "HTTP Method is required")
        public HttpMethod method;

        @NotBlank(message = "Path is required")
        public String path;

        public List<String> request = new ArrayList<>(); // Only needed when HttpMethod expects a body e.g. POST

        @NotEmpty(message = "Each endpoint must have at least one variant / response")
        @Valid
        public List<VariantConfig> responses = new ArrayList<>();

        @Target({ ElementType.TYPE })
        @Retention(RetentionPolicy.RUNTIME)
        @Constraint(validatedBy = NoHotReloadPathValidator.class)
        @Documented
        public @interface NoHotReloadPath {
            String message() default "Endpoint path is reserved for internal use";

            Class<?>[] groups() default {};

            Class<? extends Payload>[] payload() default {};
        }

        public static class NoHotReloadPathValidator implements ConstraintValidator<NoHotReloadPath, MockraConfig.EndpointConfig> {
            @Override
            public boolean isValid(MockraConfig.EndpointConfig endpoint, ConstraintValidatorContext context) {
                if (endpoint == null || endpoint.getPath() == null) {
                    return true;
                }

                if (endpoint.getPath().equals("/admin/config")) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate("Endpoint path '" + endpoint.getPath() + "' is reserved and cannot be used")
                           .addPropertyNode("path")
                           .addConstraintViolation();
                    return false;
                }

                return true;
            }
        }

        @Target(ElementType.TYPE)
        @Retention(RetentionPolicy.RUNTIME)
        @Constraint(validatedBy = RequestBodyValidator.class)
        @Documented
        public @interface ValidRequestBody {

            String message() default "Invalid request body for HTTP method";

            Class<?>[] groups() default {};

            Class<? extends Payload>[] payload() default {};
        }

        public static class RequestBodyValidator
                implements ConstraintValidator<ValidRequestBody, MockraConfig.EndpointConfig> {
            @Override
            public boolean isValid(MockraConfig.EndpointConfig endpoint, ConstraintValidatorContext context) {
                if (endpoint == null || endpoint.method == null) {
                    return true;
                }

                boolean hasBody = endpoint.request != null && !endpoint.request.isEmpty();

                boolean valid = (endpoint.method == HttpMethod.GET || endpoint.method == HttpMethod.DELETE) ? !hasBody
                        : hasBody;

                if (!valid) {
                    context.disableDefaultConstraintViolation();
                    context.buildConstraintViolationWithTemplate(endpoint.method + " method rule violated")
                            .addPropertyNode("request").addConstraintViolation();
                }

                return valid;
            }
        }
    }

    @Data
    public static class VariantConfig {
        @NotBlank(message = "Variant type is required")
        public String variant;

        @NotNull(message = "Response status is required")
        @Min(100)
        @Max(599)
        public Integer status;

        public Map<String, String> body = new HashMap<>();

        @Min(0)
        public Integer delayMs;
    }
}

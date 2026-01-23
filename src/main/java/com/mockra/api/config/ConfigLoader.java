package com.mockra.api.config;

import static com.mockra.api.errorHandling.ErrorHandler.displayMessage;
import com.mockra.api.errorHandling.ErrorType;
import com.mockra.api.config.MockraConfig.EndpointConfig;
import com.mockra.api.errorHandling.ConfigExceptions;
import com.mockra.api.errorHandling.ConfigExceptions.IllegalConfigException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolation;

public class ConfigLoader {
    public static MockraConfig loadAndValidate(Path path) throws IOException, IllegalConfigException {
        LoaderOptions options = new LoaderOptions();
        options.setAllowDuplicateKeys(false);
        options.setMaxAliasesForCollections(50);
        options.setNestingDepthLimit(50);

        Yaml yaml = new Yaml(new Constructor(MockraConfig.class, options));

        try (InputStream inputStream = Files.newInputStream(path)) {
            MockraConfig config = yaml.load(inputStream);

            validateConfig(config);

            displayMessage("Config loaded successfully", ErrorType.INFO);

            return config;
        }
    }

    public static void validateConfig(MockraConfig config) throws IllegalConfigException {
        if (config == null) {
            throw new IllegalConfigException("Config is empty or invalid YAML");
        }

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<MockraConfig>> violations = validator.validate(config);

        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder("Config validation failed:\n");
            for (ConstraintViolation<MockraConfig> v : violations) {
                sb.append("\t- ").append(v.getPropertyPath()).append(": ").append(v.getMessage()).append("\n");
            }
            throw new IllegalConfigException(sb.toString());
        }

        // Check for duplicate ids and paths
        List<String> ids = new ArrayList<>();
        List<String> paths = new ArrayList<>();

        for (EndpointConfig endpoint : config.getEndpoints()) {
            if (ids.contains(endpoint.id))
                throw new IllegalConfigException(
                        "Config validation failed:\n\tDuplicate endpoint ids were found: " + endpoint.id);
            if (paths.contains(endpoint.path))
                throw new IllegalConfigException(
                        "Config validation failed:\n\tDuplicate endpoint paths were found: " + endpoint.path);
            ids.add(endpoint.id);
            paths.add(endpoint.path);
        }
    }
}

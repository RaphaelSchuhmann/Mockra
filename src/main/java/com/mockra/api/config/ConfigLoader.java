package com.mockra.api.config;

import static com.mockra.api.errorHandling.ErrorHandler.displayMessage;
import com.mockra.api.errorHandling.ConfigExceptions;
import com.mockra.api.errorHandling.ErrorType;
import com.mockra.api.errorHandling.ConfigExceptions.IllegalConfigException;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.List;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import jakarta.validation.Validation;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.Validator;
import jakarta.validation.ConstraintViolation;

public class ConfigLoader {
    public static MockraConfig loadAndValidate() throws IOException, IllegalConfigException {
        LoaderOptions options = new LoaderOptions();
        options.setAllowDuplicateKeys(false);
        options.setMaxAliasesForCollections(50);
        options.setNestingDepthLimit(50);

        Yaml yaml = new Yaml(new Constructor(MockraConfig.class, options));

        try (InputStream inputStream = Files.newInputStream(Path.of("C:/test/config.yaml"))) {
            MockraConfig config = yaml.load(inputStream);

            validateConfig(config);

            displayMessage("Config validated", ErrorType.INFO);

            return config;
        }
    }

    public static void validateConfig(MockraConfig config) throws IllegalConfigException {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        Set<ConstraintViolation<MockraConfig>> violations = validator.validate(config);

        if (!violations.isEmpty()) {
            StringBuilder sb = new StringBuilder("Config validation failed:\n");
            for (ConstraintViolation<MockraConfig> v : violations) {
                sb.append("- ").append(v.getPropertyPath()).append(": ").append(v.getMessage()).append("\n");
            }
            throw new IllegalConfigException(sb.toString());
        }
    }
}

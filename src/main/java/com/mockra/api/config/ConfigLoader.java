package com.mockra.api.config;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

public class ConfigLoader {
    public MockraConfig loadAndValidate() throws Exception {
        LoaderOptions options = new LoaderOptions();
        options.setAllowDuplicateKeys(false);
        options.setMaxAliasesForCollections(50);
        options.setNestingDepthLimit(50);

        Yaml yaml = new Yaml(new Constructor(MockraConfig.class, options));

        try (InputStream inputStream = Files.newInputStream(Path.of("C:/test/config.yaml"))) {
            MockraConfig config = yaml.load(inputStream);
            System.out.println(config);
            return config;
        }
    }
}

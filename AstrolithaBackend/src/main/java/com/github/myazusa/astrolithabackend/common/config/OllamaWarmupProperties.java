package com.github.myazusa.astrolithabackend.common.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "ollama")
public class OllamaWarmupProperties {
    private List<Model> warmup;

    @Setter
    @Getter
    public static class Model {
        private String name;
        private String url;
        private String prompt;

    }
}
